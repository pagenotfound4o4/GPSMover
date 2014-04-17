package org.unlucky.gpsmover.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.unlucky.gpsmover.app.db.FavoriteLocation;
import org.unlucky.gpsmover.app.db.FavoritesHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A dialog with listview that show user's favourite locations.
 */
public class FavLocationDialogFragment extends DialogFragment {

    public interface FavLocationDialogListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    private FavLocationDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FavLocationDialogListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FavLocationDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_fav_location, null);
        final ListView mListView = (ListView)view.findViewById(R.id.fav_location_list);
        mListView.setAdapter(new FavLocationListAdapter(getActivity(),
                R.layout.list_item_fav_location, getData()));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onItemClick(parent, view, position, id);
                dismiss();
            }
        });
        builder.setTitle(getString(R.string.dialog_fav_location_title))
                .setIcon(R.drawable.ic_menu_star)
                .setView(view);

        return builder.create();
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Cursor cursor = FavoritesHelper.getInstance(getActivity()).open().queryAll();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                double lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double lng = cursor.getDouble(cursor.getColumnIndex("longitude"));
                int zoomLevel = cursor.getInt(cursor.getColumnIndex("zoomlevel"));
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("favorite", new FavoriteLocation(id, title, lat, lng, zoomLevel));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
