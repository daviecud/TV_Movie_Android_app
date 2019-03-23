package com.example.tvmoviefun;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;

    private TMDbApi api;

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }
        return repository;
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MoviesResponse> call = new Callback<MoviesResponse>() {
//        Log.d("MoviesRepository", "Next Page = " + page);
//        api.getPopularMovies("2d02504c55ff8d33312fa81b2675e32c", LANGUAGE, page)
//                .enqueue(new Callback<MoviesResponse>() {

//

                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        callback.onError();
                    }
                };

            switch (sortBy) {
                case TOP_RATED:
                    api.getTopRatedMovies(BuildConfig.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                    break;
                case UPCOMING:
                    api.getUpcomingMovies(BuildConfig.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                    break;
                case POPULAR:
                api.getPopularMovies(BuildConfig.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                     break;
            }
    }
        public void getGenres ( final OnGetGenresCallback callback){
            api.getGenres(BuildConfig.API_KEY, LANGUAGE)
                    .enqueue(new Callback<GenresResponse>() {
                        @Override
                        public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                            if (response.isSuccessful()) {
                                GenresResponse genresResponse = response.body();
                                if (genresResponse != null && genresResponse.getGenres() != null) {
                                    callback.onSuccess(genresResponse.getGenres());
                                } else {
                                    callback.onError();
                                }
                            } else {
                                callback.onError();
                            }
                        }

                        @Override
                        public void onFailure(Call<GenresResponse> call, Throwable t) {
                            callback.onError();

                        }
                    });
        }

        public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, BuildConfig.API_KEY, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                        if (movie != null) {callback.onSuccess(movie);
                        } else {
                            callback.onError();
                        }
                        } else {
                            callback.onError();
                        }
                        }

                        @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                            callback.onError();
                        }
                    });

        }

}




