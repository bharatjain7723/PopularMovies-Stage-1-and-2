package com.example.android.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a object to hold trailers.
 * </p>
 * POJO is created with jsonschema2pojo.
 * http://www.jsonschema2pojo.org/
 */
public class TrailersWrapper {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = new ArrayList<>();

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<Trailer> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
