package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ViewMovie extends BaseActivity implements IParser{

    private int id;
    private Movie movie;
    private RelativeLayout mainPage;
    private ImageView poster;
    private TextView title, year, genres, rating, synopsis, genreHeading, tagline, release,
            status, runtime, languages, collection, revenue, budget, orig_title,
            prod_countries, prod_companies, collectionHeading, languagesHeading, prod_companiesHeading, prod_countriesHeading;
    private Bitmap bitmap1;
    private URL url1;
    private int page=1;
    private int movieID;
    private ImageButton favButton, youtubeButton;
    private DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        db = new DBHelper(this);

        //gets the passed movie id
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("passedID")){
            movieID = intent.getIntExtra("passedID", 0);
            if(db.movieExists(movieID)){
                movie = new Movie(this, movieID);
                init(movie);
            }
            else{
                new ApiHelper(this).SetMovieIDQuery(movieID).execute();
            }
        }
    }

    private void init(Movie movie){

        mainPage = (RelativeLayout) findViewById(R.id.mainpage);
        poster = (ImageView) findViewById(R.id.poster);


        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        genres = (TextView) findViewById(R.id.genres);
        genreHeading = (TextView) findViewById(R.id.genreHeading);
        rating = (TextView) findViewById(R.id.rating);
        synopsis = (TextView) findViewById(R.id.synopsis);
        tagline = (TextView) findViewById(R.id.tagline);
        release = (TextView) findViewById(R.id.releasedate);
        status = (TextView) findViewById(R.id.status);
        runtime = (TextView) findViewById(R.id.runtime);
        languages = (TextView) findViewById(R.id.languages);
        collection = (TextView) findViewById(R.id.collecion);
        revenue = (TextView) findViewById(R.id.revenue);
        budget = (TextView) findViewById(R.id.budget);
        orig_title = (TextView) findViewById(R.id.original_title);
        prod_countries = (TextView) findViewById(R.id.production_countries);
        prod_companies = (TextView) findViewById(R.id.production_companies);
        collectionHeading = (TextView) findViewById(R.id.collectionHeading);
        languagesHeading = (TextView) findViewById(R.id.languagesHeading);
        prod_companiesHeading = (TextView) findViewById(R.id.companiesHeading);
        prod_countriesHeading = (TextView) findViewById(R.id.countriesHeading);

        favButton = (ImageButton)findViewById(R.id.addFavButton);
        if(db.movieExists(movie.getId())){
            favButton.setImageResource(R.mipmap.fav_yes);
        }
        new Thread(new AddRemoveMovie()).start();
        youtubeButton = (ImageButton)findViewById(R.id.youtubeButton);
        new Thread(new YoutubeListener()).start();

        title.setText(movie.getTitle());
        year.setText("" + movie.getYear());
        rating.setText("" + movie.getRating());
        synopsis.setText(movie.getSynopsis());
        tagline.setText(movie.getTagline());
        release.setText(movie.getReleaseDate());
        status.setText(movie.getStatus());
        runtime.setText("" + movie.getRuntime() + " minutes");
        revenue.setText("$" + movie.getRevenue());
        budget.setText("$" + movie.getBudget());
        orig_title.setText(movie.getOriginalTitle());

        if(movie.getCollection() != null) {
            collectionHeading.setText("Collection :");
            collection.setText(movie.getCollection().getName());
        }

        if(movie.getSpokenLanguages().size() >0) {
            languagesHeading.setText("Spoken Languages(s):");
            languages.setText(createArrayDropLineString(movie.getSpokenLanguages()));
        }

        if(movie.getProductionCompanyNames().size() >0) {
            prod_companiesHeading.setText("Production Companies :");
            prod_companies.setText(createArrayDropLineString(movie.getProductionCompanyNames()));
        }

        if(movie.getProductionCountries().size() >0) {
            prod_countriesHeading.setText("Production Countries :");
            prod_countries.setText(createArrayDropLineString(movie.getProductionCountries()));
        }

        if(movie.getGenres().size()>0) {
            genreHeading.setText("Genre(s):");
            genres.setText(createArrayString(movie.getGenres()));
        }
        if(!movie.getPoster().equals(null)) {
            setImages();
        }
    }

    // Changed this as it was crashing when loading from database with no internet connection
    private void setImages() {
        Picasso.with(this).load(movie.getPoster()).into(poster);
        Picasso.with(this).load(movie.getPoster()).placeholder(R.drawable.moviereel).into(
                new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mainPage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Toast.makeText(getApplicationContext(), "Failed To Load Background Image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                }
        );
        /*try {
            url1 = new URL(movie.getPoster());
            bitmap1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
            Drawable dr = new BitmapDrawable(getResources(), bitmap1);
            mainPage.setBackground(dr);
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.w("ViewMovie", "Exception creating image");
        }*/
    }

    private String createArrayString(List<String> items) {
        String genreString="";
        genreString = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            genreString += " / " +  items.get(i);
        }
        return genreString;
    }

    private String createArrayDropLineString(List<String> items) {
        String genreString="";
        genreString = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            genreString += "\n" +  items.get(i);
        }
        return genreString;
    }



    public void parseJson(String json) {
       try {
           Gson gson = new Gson();
           movie = gson.fromJson(json, Movie.class);
           init(movie);
       }
       catch(Exception ex){
           if(ex == null) {
               Log.d("EXCEPTION", "NULL");
           } else if (ex.getMessage() == null){
               ex.printStackTrace();
           } else {
               Log.d("EXCEPTION", ex.getMessage());
           }
        }
    }

    class AddRemoveMovie implements Runnable{
        @Override
        public void run() {
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(db.addRemoveMovie(movie)){
                        Toast.makeText(ViewMovie.this, "Movie Added To Favourites", Toast.LENGTH_SHORT).show();
                        favButton.setImageResource(R.mipmap.fav_yes);
                    }
                    else{
                        Toast.makeText(ViewMovie.this, "Movie Removed From Favourites", Toast.LENGTH_SHORT).show();
                        favButton.setImageResource(R.mipmap.fav_no);
                    }
                }
            });
        }
    }

    class YoutubeListener implements Runnable{
        @Override
        public void run() {
            youtubeButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), YoutubeActivity.class);
                    intent.putExtra("passedID", "0WWzgGyAH6Y");
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
