package com.wesleyramos.codehero.Characters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wesleyramos.codehero.Model.Character;
import com.wesleyramos.codehero.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CharactersAdapter extends BaseAdapter {

    private List<Character> mCharacters;
    private static Activity sActivity;

    public CharactersAdapter(List<Character> mCharacters, Activity sActivity) {
        this.mCharacters = mCharacters;
        this.sActivity = sActivity;
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public Object getItem(int position) {
        return mCharacters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCharacters.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = sActivity.getLayoutInflater().inflate(R.layout.item_list_character, parent, false);

        Character character = mCharacters.get(position);

        TextView tvNome = (TextView) view.findViewById(R.id.itemListCharacter_name);
        CircleImageView ciThumbnail = (CircleImageView) view.findViewById(R.id.itemListCharacter_ciThumbnail);

        tvNome.setText(character.getName());
        Glide.with(sActivity).load(character.getThumbnail()).into(ciThumbnail);

        return view;
    }
}
