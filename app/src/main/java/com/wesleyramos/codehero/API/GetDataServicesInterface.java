package com.wesleyramos.codehero.API;

import com.wesleyramos.codehero.API.DTO.Characters.CharactersDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GetDataServicesInterface {

    @GET("/v1/public/characters")
    Call<CharactersDTO> getCharacters(@QueryMap Map<String, String> querys,
                                      @QueryMap Map<String, Integer> paginate);

}
