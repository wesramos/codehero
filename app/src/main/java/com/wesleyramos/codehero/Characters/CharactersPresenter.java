package com.wesleyramos.codehero.Characters;

import android.content.Context;

import com.wesleyramos.codehero.API.DTO.Characters.CharactersDTO;
import com.wesleyramos.codehero.API.DTO.Characters.Result;
import com.wesleyramos.codehero.API.ServiceMarvel;
import com.wesleyramos.codehero.Model.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharactersPresenter implements CharactersInterface.IPresenter {

    private CharactersInterface.IView view;
    private int mCurrentPage;
    private List<Character> heroes;
    private String keyword;
    private Context mContext;

    public CharactersPresenter(CharactersInterface.IView view, Context mContext) {
        this.view = view;
        this.heroes = new ArrayList<>();
        this.mContext = mContext;
        this.keyword = "";
        this.mCurrentPage = 1;
    }

    @Override
    public void setKeyword(String keyword) {
        this.keyword = keyword;
        this.mCurrentPage = 1;
        this.loadCharacters();
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }

    @Override
    public void cleanKeyword() {
        this.keyword = "";
        this.mCurrentPage = 1;
        this.loadCharacters();
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public List<Character> getListOfCharacters() {
        return heroes;
    }

    @Override
    public void updatePage(int page) {
        this.mCurrentPage = page;
        this.loadCharacters();
    }

    @Override
    public void nextPage() {
        this.mCurrentPage++;
        this.loadCharacters();
    }

    @Override
    public void previousPage() {
        this.mCurrentPage--;
        this.loadCharacters();
    }

    @Override
    public void loadCharacters() {
        view.showLoadingProgress(true);
        view.showListView(false);

        HashMap<String, String> querys = ServiceMarvel.getAuth(mContext);
        HashMap<String, Integer> paginate = new HashMap<>();

        if (!getKeyword().isEmpty()) {
            querys.put("nameStartsWith", getKeyword());
        } else {
            querys.remove("nameStartsWith");
        }

        paginate.put("limit", 4);
        paginate.put("offset", (this.getCurrentPage() == 1 ? 0 : this.getCurrentPage() - 1) * 4);

        ServiceMarvel
                .getInstance()
                .getCharacters(querys, paginate)
                .enqueue(new Callback<CharactersDTO>() {
                    @Override
                    public void onResponse(Call<CharactersDTO> call, Response<CharactersDTO> response) {
                        if (response.isSuccessful()) {
                            heroes.clear();

                            for (Result result : response.body().getData().getResults()) {
                                heroes.add(new Character(
                                        result.getId(),
                                        result.getName(),
                                        result.getThumbnail()
                                                .getPath()
                                                .concat("/standard_medium.")
                                                .concat(result.getThumbnail().getExtension())
                                                .replace("http", "https"),
                                        result.getDescription(),
                                        result.getModified()
                                ));
                            }

                            view.updateAdapter();
                            view.showLoadingProgress(false);
                            view.showListView(true);

                            int total_characters = response.body().getData().getTotal();
                            boolean disableButtonNextPage;

                            if (total_characters > 0) {
                                if (getCurrentPage() == 0) {
                                    disableButtonNextPage = total_characters <= 4;
                                } else {
                                    disableButtonNextPage = total_characters <= getCurrentPage() * 4;
                                }
                            } else {
                                disableButtonNextPage = true;
                            }

                            view.disableButtonNextPage(disableButtonNextPage);

                            boolean disableButtonPreviousPage = getCurrentPage() <= 1;
                            view.disableButtonPreviousPage(disableButtonPreviousPage);

                            view.updateButtonsPaginate(getCurrentPage(), total_characters, disableButtonPreviousPage, disableButtonNextPage);
                        }
                    }

                    @Override
                    public void onFailure(Call<CharactersDTO> call, Throwable t) {
                        view.showLoadingProgress(false);
                        view.showErrorLoadingCharacters(true);
                    }
                });

    }
}