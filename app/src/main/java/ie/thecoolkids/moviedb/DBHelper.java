package ie.thecoolkids.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 19;
    private static final String DB_NAME = "favourites.db", TABLE_MOVIES = "movies", TABLE_TVSHOWS = "tvshows";
    private static final String ID = "id", TITLE = "title", RELEASE = "releaseDate",
                                RATING = "rating", SYNOPSIS = "synopsis", TAGLINE = "tagline",
                                STATUS = "status", RUNTIME = "runtime", REVENUE = "revenue",
                                BUDGET = "budget", ORIGINAL_TITLE = "originalTitle", GENRES = "genres",
                                POSTER = "poster", COLLECTION = "collection", LANGUAGES = "languages",
                                PRODUCTION_COMPANIES = "productionCompanies", PRODUCTION_COUNTRIES = "productionCountries",
                                FIRST_AIR = "firstAir", LAST_AIR = "lastAir", NETWORKS = "networks",
                                SEASONS = "seasons", EPISODES = "episodes", IN_PRODUCTION = "inProduction",
                                CREATED_BY = "createdBy", TYPE = "type", VIDEO_KEY = "videoKey";


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIES + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        TITLE + " TEXT," + RELEASE + " TEXT," +
                        RATING + " REAL," + SYNOPSIS + " TEXT," +
                        TAGLINE + " TEXT," + STATUS + " TEXT," +
                        RUNTIME + " INTEGER," + REVENUE + " INTEGER," +
                        BUDGET + " INTEGER," + ORIGINAL_TITLE + " TEXT," +
                        GENRES + " TEXT," + POSTER + " TEXT," +
                        COLLECTION + " TEXT," + LANGUAGES + " TEXT," +
                        PRODUCTION_COMPANIES + " TEXT," + PRODUCTION_COUNTRIES + " TEXT," +
                        VIDEO_KEY + " TEXT);"
        );
        db.execSQL(
                "CREATE TABLE " + TABLE_TVSHOWS + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        TITLE + " TEXT," + FIRST_AIR + " TEXT," +
                        LAST_AIR + " TEXT," + RATING + " REAL," +
                        SYNOPSIS + " TEXT," + STATUS + " TEXT," +
                        RUNTIME + " TEXT," + GENRES + " TEXT," +
                        POSTER + " TEXT," + LANGUAGES + " TEXT," +
                        NETWORKS + " TEXT," + PRODUCTION_COMPANIES + " TEXT," +
                        PRODUCTION_COUNTRIES + " TEXT," + SEASONS + " INTEGER," +
                        EPISODES + " INTEGER," + IN_PRODUCTION + " TEXT," +
                        CREATED_BY + " TEXT," + TYPE + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TVSHOWS + ";");
        onCreate(db);
    }

    public boolean addRemoveMovie(Movie movie){
        int id = movie.getId();
        if(movieExists(id)){
            getWritableDatabase().delete(TABLE_MOVIES, ID + " = " + id, null);
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(TITLE, movie.getTitle());
        values.put(RELEASE, movie.getReleaseDate());
        values.put(RATING, movie.getRating());
        values.put(SYNOPSIS, movie.getSynopsis());
        values.put(TAGLINE, movie.getTagline());
        values.put(STATUS, movie.getStatus());
        values.put(RUNTIME, movie.getRuntime());
        values.put(REVENUE, movie.getRevenue());
        values.put(BUDGET, movie.getBudget());
        values.put(ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(GENRES, asString(movie.getGenres()));
        values.put(POSTER, movie.getPoster());
        values.put(COLLECTION, (movie.getBelongsToCollection()) ? movie.getCollection().getName() : "");
        values.put(LANGUAGES, asString(movie.getSpokenLanguages()));
        values.put(PRODUCTION_COMPANIES, asString(movie.getProductionCompanyNames()));
        values.put(PRODUCTION_COUNTRIES, asString(movie.getProductionCountries()));
        values.put(VIDEO_KEY, movie.getVideoKey());
        if(this.getWritableDatabase().insert(TABLE_MOVIES, null, values) == -1){return false;}
        else{return true;}
    }

    public boolean addRemoveTvShow(TvShow tvShow){
        int id = tvShow.getId();
        if(tvShowExists(id)){
            getWritableDatabase().delete(TABLE_TVSHOWS, ID + " = " + id, null);
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(TITLE, tvShow.getTitle());
        values.put(FIRST_AIR, tvShow.getFirstAirDate());
        values.put(LAST_AIR, tvShow.getLastAirDate());
        values.put(RATING, tvShow.getRating());
        values.put(SYNOPSIS, tvShow.getSynopsis());
        values.put(STATUS, tvShow.getStatus());
        values.put(RUNTIME, asString(tvShow.getRuntimes()));
        values.put(GENRES, asString(tvShow.getGenres()));
        values.put(POSTER, tvShow.getPoster());
        values.put(LANGUAGES, asString(tvShow.getLanguages()));
        values.put(NETWORKS, asString(tvShow.getNetworks()));
        values.put(PRODUCTION_COMPANIES, asString(tvShow.getProductionCompanies()));
        values.put(PRODUCTION_COUNTRIES, asString(tvShow.getOriginCountry()));
        values.put(SEASONS, tvShow.getNumberOfSeasons());
        values.put(EPISODES, tvShow.getNumberOfEpisodes());
        values.put(IN_PRODUCTION, tvShow.getInProduction());
        values.put(CREATED_BY, asString(tvShow.getCreatedBy()));
        values.put(TYPE, tvShow.getType());
        if(this.getWritableDatabase().insert(TABLE_TVSHOWS, null, values) == -1){return false;}
        else{return true;}
    }

    public boolean movieExists(int id){
        String sql = "SELECT * FROM " + TABLE_MOVIES + " WHERE " + ID + " = " + id;
        if(this.getWritableDatabase().rawQuery(sql, null).getCount() == 0){
            return false;
        }
        return true;
    }

    public boolean tvShowExists(int id){
        String sql = "SELECT * FROM " + TABLE_TVSHOWS + " WHERE " + ID + " = " + id;
        if(this.getWritableDatabase().rawQuery(sql, null).getCount() == 0){
            return false;
        }
        return true;
    }

    private String asString(List list){
        String s = "";
        int n = list.size();
        for(int i = 0; i < n; i++){
            s += list.get(i) + ";";
        }
        return s;
    }

    public Cursor getAllLocalMovies(){
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
    }

    public Cursor getAllLocalTvShows(){
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_TVSHOWS, null);
    }

    public Cursor getMovie(int id){
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_MOVIES + " WHERE " + ID + " = " + id, null);
    }

    public Cursor getTvShow(int id){
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_TVSHOWS + " WHERE " + ID + " = " + id, null);
    }
}
