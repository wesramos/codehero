package com.wesleyramos.codehero.Characters;

import com.wesleyramos.codehero.Model.Character;

import java.util.List;

public interface CharactersInterface {
    interface IView {
        void updateAdapter();

        void showLoadingProgress(boolean show);

        void showListView(boolean show);

        void showErrorLoadingCharacters(boolean show);

        void disableButtonNextPage(boolean disable);

        void disableButtonPreviousPage(boolean disable);

        void updateButtonsPaginate(int currentPage, int totalItems, boolean btPreviousPageDisabled, boolean btNextPageDisabled);

        void resetVisibilityPaginate();

        void setVisibilityPaginate(int currentPage, int totalItems);

        void setTextAndColorPaginate(int currentPage, boolean btPreviousPageDisabled, boolean btNextPageDisabled);
    }

    interface IPresenter {
        void setKeyword(String keyword);

        String getKeyword();

        void cleanKeyword();

        int getCurrentPage();

        List<Character> getListOfCharacters();

        void updatePage(int page);

        void nextPage();

        void previousPage();

        void loadCharacters();
    }
}
