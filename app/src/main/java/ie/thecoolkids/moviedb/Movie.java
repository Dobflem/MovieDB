package ie.thecoolkids.moviedb;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable, TheMovieDB, IListItem {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    private boolean adult;
    private String backdrop_path;
    private Collection belongs_to_collection;
    private int budget;
    private Genre[] genres;
    private String homepage;
    private int id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private float popularity;
    public String poster_path;
    private ProductionCompany[] production_companies;
    private ProductionCountry[] production_countries;
    private String release_date;
    private int revenue;
    private int runtime;
    private SpokenLanguage[] spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private float vote_average;
    private float vote_count;
    private String video_key;

    Movie(){
    }

    Movie(Context context, int id){
        DBHelper db = new DBHelper(context);
        Cursor c = db.getMovie(id);
        c.moveToFirst();
        this.id = c.getInt(0);
        this.title = c.getString(1);
        this.release_date = c.getString(2);
        this.vote_average = c.getFloat(3);
        this.overview = c.getString(4);
        this.tagline = c.getString(5);
        this.status = c.getString(6);
        this.runtime = c.getInt(7);
        this.revenue = c.getInt(8);
        this.budget = c.getInt(9);
        this.original_title = c.getString(10);
        setGenres(c.getString(11));
        this.poster_path = c.getString(12);
        setCollection(c.getString(13));
        setSpokenLanguages(c.getString(14));
        setProductionCompanies(c.getString(15));
        setProductionCountries(c.getString(16));
        setVideoKey(c.getString(17));
    }


    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public boolean getBelongsToCollection() {
        boolean inCollection;
        if(belongs_to_collection == null)   inCollection = false;
        else                                inCollection = true;
        return inCollection;
    }

    public Collection getCollection() {
        return belongs_to_collection;
    }

    public void setCollection(String s){
        this.belongs_to_collection = new Collection(s);
    }

    public int getBudget() {
        return budget;
    }

    public List<String> getGenres(){
        List<String> genre_list = new ArrayList<>();
        for(int i=0; i< genres.length ; i++){
            genre_list.add(genres[i].getName());
        }
        return genre_list;
    }

    public void setGenres(String s){
        String array[] = s.split(";");
        int n = array.length;
        genres = new Genre[n];
        for(int i = 0; i < n; i++){
            genres[i] = new Genre(array[i]);
        }
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getImdbId() {
        return imdb_id;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getSynopsis() {
        return overview;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getPoster(){
        if(poster_path != null){
            if(poster_path.contains(BASE_URL)){
                return poster_path;
            }
            else{
                return String.format("%s%s", BASE_URL, poster_path);
            }
        }
        else                    return null;
    }

    public void setPoster(String poster_path){
        this.poster_path = poster_path;
    }

    public ProductionCompany[] getProductionCompanies() {
        return production_companies;
    }

    public List<String> getProductionCompanyNames(){
        List<String> production_company_list = new ArrayList<>();
        for(int i=0; i< production_companies.length ; i++){
            production_company_list.add(production_companies[i].getName());
        }
        return production_company_list;
    }

    public void setProductionCompanies(String s){
        String array[] = s.split(";");
        int n = array.length;
        production_companies = new ProductionCompany[n];
        for(int i = 0; i < n; i++){
            production_companies[i] = new ProductionCompany(array[i]);
        }
    }

    public int getYear() {
        return Integer.parseInt(release_date.substring(0, 4));
    }

    public String getReleaseDate() {
        return release_date;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<String> getSpokenLanguages(){
        List<String> languages_list = new ArrayList<>();
        for(int i=0; i< spoken_languages.length ; i++){
            languages_list.add(spoken_languages[i].getName());
        }
        return languages_list;
    }

    public void setSpokenLanguages(String s){
        String array[] = s.split(";");
        int n = array.length;
        spoken_languages = new SpokenLanguage[n];
        for(int i = 0; i < n; i++){
            spoken_languages[i] = new SpokenLanguage(array[i]);
        }
    }

    public List<String> getProductionCountries(){
        List<String> countries_list = new ArrayList<>();
        for(int i=0; i< production_countries.length ; i++){
            countries_list.add(production_countries[i].getName());
        }
        return countries_list;
    }

    public void setProductionCountries(String s){
        String array[] = s.split(";");
        int n = array.length;
        production_countries = new ProductionCountry[n];
        for(int i = 0; i < n; i++){
            production_countries[i] = new ProductionCountry(array[i]);
        }
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public boolean getVideo() {
        return video;
    }

    public float getRating() {
        return vote_average;
    }

    public void setRating(float rating){
        this.vote_average = rating;
    }

    public float getVoteCount() {
        return vote_count;
    }

    public View getRowView(final Context context, LayoutInflater inflater) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.movie_list, null);

        /*
         * TODO: I took this whole Holder stuff from an online tutorial,
         * but it seems a bit redundant, so I might remove it soon.
         * It does, show us exactly what we can use in the UI though,
         * so I'm not sure yet.
         * The following connects the Holder to the UI controls.
         */
        holder.imgPoster = (ImageView) rowView.findViewById(R.id.imgPoster);
        holder.tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        holder.ratBar = (RatingBar) rowView.findViewById(R.id.ratBar);

        /*
         * This is where we give information to the UI.
         */
        holder.tvTitle.setText(this.getTitle());
        holder.ratBar.setRating(this.getRating() / 2);

        /*
         * This is where we load in the image.
         * Use Picasso's Placeholder to put a Stock image in the Movie Poster first.
         */
        Picasso.with(context).load(this.getPoster()).placeholder(R.drawable.movies).fit().into(holder.imgPoster);

        /*
         * This sets up the List Items OnClickListener Event.
         * When a List Item gets clicked, it brings us to the Movie's Info Activity.
         */
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).Kill = false;
                Intent intent = new Intent(v.getContext(), ViewMovie.class);
                intent.putExtra("passedID", getId());
                v.getContext().startActivity(intent);
            }
        });

        /* Return the List Item */
        return rowView;
    }

    public String getVideoKey() {
        return video_key;
    }

    public void setVideoKey(String video_key) {
        this.video_key = video_key;
    }
}