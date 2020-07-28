package com.example.madcampweek3.Profile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.madcampweek3.Account.AccountActivity;
import com.example.madcampweek3.R;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource

        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder

            /* Title */
            viewHolder.profileImage = cell.findViewById(R.id.profile_image);


            /* Content */
            viewHolder.contentAvatar = cell.findViewById(R.id.content_avatar);
            viewHolder.contentNameView = cell.findViewById(R.id.content_name_view);
            viewHolder.contentRatingStar = cell.findViewById(R.id.content_rating_stars);
            viewHolder.contentNumberRatingStars = cell.findViewById(R.id.content_number_rating_stars);
            viewHolder.contentFromAddress1 = cell.findViewById(R.id.content_from_address_1);
            viewHolder.contentToAddress1 = cell.findViewById(R.id.content_to_address_1);
            viewHolder.contentDate = cell.findViewById(R.id.content_date);
            viewHolder.contentIntimacy = cell.findViewById(R.id.content_intimacy);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        /* Get date */
        Calendar time = Calendar.getInstance();
        String date = (time.get(Calendar.MONTH) + 1) + "/" + time.get(Calendar.DATE);


        // bind data from selected element to view through view holder
        String contactTime = String.valueOf(item.getContactTime());
        /* Title */
        viewHolder.profileImage.setImageBitmap(item.getProfile());

//        viewHolder.contactTime.setText(contactTime);

        /* Content */
        viewHolder.contentAvatar.setImageBitmap(item.getProfile());
        viewHolder.contentNameView.setText(item.getname());
//        viewHolder.contentRatingStar; // TODO: prepare rating star image
//        viewHolder.contentNumberRatingStars = cell.findViewById(R.id.content_number_rating_stars);
        viewHolder.contentFromAddress1.setText(item.getFromAddress());
        viewHolder.contentToAddress1.setText(item.getToAddress());
        viewHolder.contentDate.setText(date);
        viewHolder.contentIntimacy.setText(contactTime);

        // set custom btn handler for list item from that item
        viewHolder.contentRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AccountActivity.class);
                intent.putExtra("friendID", item.getId());
                getContext().startActivity(intent);
            }
        });
//        if (item.getRequestBtnClickListener() != null) {
//            viewHolder.contentRequestBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), AccountActivity.class);
//                    intent.putExtra("friendID", item.getId());
//                    getContext().startActivity(intent);
//                }
//            });
//        } else {
//            // (optionally) add "default" handler if no handler found in item
////            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
//        }

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        /* Title */
        CircleImageView profileImage;
        TextView fromAddress;
        TextView toAddress;
        TextView contactTime;

        /* Content */
        ImageView contentAvatar;
        TextView contentNameView;
        ImageView contentRatingStar;
        TextView contentNumberRatingStars;
        TextView contentFromAddress1;
        TextView contentToAddress1;
        TextView contentDate;
        TextView contentIntimacy;
        TextView contentRequestBtn;
    }
}
