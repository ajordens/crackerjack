package com.littlesquare.crackerjack.services.example.clients

import retrofit.RestAdapter
import retrofit.http.GET
import retrofit.http.Path

/**
 * TODO-AJ An exception handler that will throw a RetryableClientException.
 * TODO-AJ An enhancer/wrapper that will wrap the ExampleClient with some retry semantics.
 *
 * @author Adam Jordens (adam@jordens.org)
 */
public class ExampleClient {
    private static final String API_URL = "https://api.github.com"

    static class Contributor {
        String login
        int contributions
    }

    static interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        )
    }

    public static void main(String... args) {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build()

        // Create an instance of our GitHub API interface.
        GitHub github = restAdapter.create(GitHub)

        // Fetch and print a list of the contributors to this library.
        List<Contributor> contributors = github.contributors("square", "retrofit")
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")")
        }
    }
}