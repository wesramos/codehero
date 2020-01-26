package com.wesleyramos.codehero.Characters;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.wesleyramos.codehero.Model.Character;
import com.wesleyramos.codehero.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class CharactersActivity extends AppCompatActivity implements CharactersInterface.IView {

    private CharactersPresenter presenter;
    private CharactersAdapter charactersAdapter;
    private AlertDialog alerta;

    @BindView(R.id.charactersActivity_etSeachCharacters)
    EditText edSearchCharacters;

    @BindView(R.id.charactersActivity_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.charactersActivity_lvCharacters)
    ListView lvHeroes;

    @BindView(R.id.charactersActivity_txMessageError)
    TextView txMessageError;

    @BindView(R.id.charactersActivity_btPaginateOne)
    Button btPaginateOne;

    @BindView(R.id.charactersActivity_btPaginateTwo)
    Button btPaginateTwo;

    @BindView(R.id.charactersActivity_btPaginateThree)
    Button btPaginateThree;

    @BindView(R.id.charactersActivity_ibNextPage)
    ImageButton ibNextPage;

    @BindView(R.id.charactersActivity_ibPreviousPage)
    ImageButton ibPreviousPage;

    @OnClick(R.id.charactersActivity_ibNextPage)
    void nextPage() {
        this.presenter.nextPage();
    }

    @OnClick(R.id.charactersActivity_ibPreviousPage)
    void prevousPage() {
        this.presenter.previousPage();
    }

    @OnClick(R.id.charactersActivity_btPaginateOne)
    void updatePaginateButtonOne() {
        this.presenter.updatePage(Integer.parseInt(btPaginateOne.getText().toString()));
    }

    @OnClick(R.id.charactersActivity_btPaginateTwo)
    void updatePaginateButtonTwo() {
        this.presenter.updatePage(Integer.parseInt(btPaginateTwo.getText().toString()));
    }

    @OnClick(R.id.charactersActivity_btPaginateThree)
    void updatePaginateButtonThree() {
        this.presenter.updatePage(Integer.parseInt(btPaginateThree.getText().toString()));
    }

    @OnItemClick(R.id.charactersActivity_lvCharacters)
    void onItemClick(int position) {
        Character character = (Character) charactersAdapter.getItem(position);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_details_character, null);

        view.findViewById(R.id.dialogDetailsCharacter_btDismiss)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        alerta.dismiss();
                    }
                });

        ImageView ivThumbnail = (ImageView) view.findViewById(R.id.dialogDetailsCharacter_ivTumbnail);
        TextView tvDescrition = (TextView) view.findViewById(R.id.dialogDetailsCharacter_tvDescription);
        TextView txName = (TextView) view.findViewById(R.id.dialogDetailsCharacter_tvName);

        txName.setText(character.getName());
        tvDescrition.setText(character.getDescription());
        Glide.with(this).load(character.getThumbnail().replace("standard_medium", "landscape_large")).into(ivThumbnail);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    @OnTextChanged(R.id.charactersActivity_etSeachCharacters)
    void onTextChanged(CharSequence text) {
        String textSearch = text.toString();

        if (textSearch.length() >= 2) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.setKeyword(textSearch);
                }
            }, 700);
        } else if (textSearch.length() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.cleanKeyword();
                }
            }, 700);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        ButterKnife.bind(this);

        this.instantiateObjects();
        this.presenter.loadCharacters();
    }

    private void instantiateObjects() {
        this.presenter = new CharactersPresenter(this, this);
        this.charactersAdapter = new CharactersAdapter(this.presenter.getListOfCharacters(), this);
        this.lvHeroes.setAdapter(this.charactersAdapter);
    }

    @Override
    public void updateAdapter() {
        this.charactersAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showErrorLoadingCharacters(boolean show) {
        txMessageError.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void disableButtonNextPage(boolean disable) {
        ibNextPage.setVisibility(disable ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void disableButtonPreviousPage(boolean disable) {
        ibPreviousPage.setVisibility(disable ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void updateButtonsPaginate(int currentPage, int totalItems, boolean btPreviousPageDisabled, boolean btNextPageDisabled) {
        this.resetVisibilityPaginate();
        this.setVisibilityPaginate(currentPage, totalItems);
        this.setTextAndColorPaginate(currentPage, btPreviousPageDisabled, btNextPageDisabled);
    }

    @Override
    public void showListView(boolean visible) {
        lvHeroes.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void resetVisibilityPaginate() {
        btPaginateTwo.setVisibility(View.VISIBLE);
        btPaginateThree.setVisibility(View.VISIBLE);
        btPaginateOne.setVisibility(View.VISIBLE);
    }

    @Override
    public void setVisibilityPaginate(int currentPage, int totalItems) {
        if ((currentPage * 4) < totalItems) {
            int diff = totalItems - (currentPage * 4);

            if (diff > 4) {
                btPaginateTwo.setVisibility(View.VISIBLE);
                btPaginateThree.setVisibility(View.VISIBLE);
            } else if (diff <= 0) {
                btPaginateTwo.setVisibility(View.INVISIBLE);
                btPaginateThree.setVisibility(View.INVISIBLE);
            } else {
                btPaginateTwo.setVisibility(View.VISIBLE);
                btPaginateThree.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void setTextAndColorPaginate(int currentPage, boolean btPreviousPageDisabled, boolean btNextPageDisabled) {
        if (btPreviousPageDisabled) {
            btPaginateOne.setBackground(getResources().getDrawable(R.drawable.background_button_paginate_selected));
            btPaginateTwo.setBackground(getResources().getDrawable(R.drawable.background_button_paginate));
            btPaginateThree.setBackground(getResources().getDrawable(R.drawable.background_button_paginate));

            btPaginateOne.setTextColor(getResources().getColor(android.R.color.white));
            btPaginateTwo.setTextColor(getResources().getColor(android.R.color.black));
            btPaginateThree.setTextColor(getResources().getColor(android.R.color.black));

            btPaginateOne.setText(String.valueOf(currentPage));
            btPaginateTwo.setText(String.valueOf(currentPage + 1));
            btPaginateThree.setText(String.valueOf(currentPage + 2));

        } else if (btNextPageDisabled) {
            btPaginateOne.setBackground(getResources().getDrawable(R.drawable.background_button_paginate));
            btPaginateTwo.setBackground(getResources().getDrawable(R.drawable.background_button_paginate));
            btPaginateThree.setBackground(getResources().getDrawable(R.drawable.background_button_paginate_selected));

            btPaginateOne.setTextColor(getResources().getColor(android.R.color.black));
            btPaginateTwo.setTextColor(getResources().getColor(android.R.color.black));
            btPaginateThree.setTextColor(getResources().getColor(android.R.color.white));

            if ((currentPage - 2) == 0) {
                btPaginateOne.setVisibility(View.INVISIBLE);
            }

            btPaginateOne.setText(String.valueOf(currentPage - 2));
            btPaginateTwo.setText(String.valueOf(currentPage - 1));
            btPaginateThree.setText(String.valueOf(currentPage));
        } else {
            btPaginateOne.setBackground(getResources().getDrawable(R.drawable.background_button_paginate));
            btPaginateTwo.setBackground(getResources().getDrawable(R.drawable.background_button_paginate_selected));
            btPaginateThree.setBackground(getResources().getDrawable(R.drawable.background_button_paginate));

            btPaginateOne.setTextColor(getResources().getColor(android.R.color.black));
            btPaginateTwo.setTextColor(getResources().getColor(android.R.color.white));
            btPaginateThree.setTextColor(getResources().getColor(android.R.color.black));

            btPaginateOne.setVisibility(View.VISIBLE);

            btPaginateOne.setText(String.valueOf(currentPage - 1));
            btPaginateTwo.setText(String.valueOf(currentPage));
            btPaginateThree.setText(String.valueOf(currentPage + 1));
        }
    }
}
