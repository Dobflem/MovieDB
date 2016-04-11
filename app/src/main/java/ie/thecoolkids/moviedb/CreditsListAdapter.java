package ie.thecoolkids.moviedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CreditsListAdapter extends BaseAdapter {

    private List<Role> roles = null;
    private static LayoutInflater inflater = null;
    private ViewPerson personview;

    public CreditsListAdapter(ViewPerson _person) {
        personview = _person;
        inflater = (LayoutInflater) personview.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRoles(List<Role> _roles) {
        roles = _roles;
    }

    @Override
    public int getCount() {
        return roles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        ImageView rolePoster;
        TextView nameOrTitle, mediaType, characterOrJobHeading, characterOrJob,
                epCountOrDeptHeading, epCountOrDept, dateHeading, date;
    }

    /*
     * We don't call this function, it automatically gets called.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.role_list, null);

        holder.rolePoster = (ImageView) rowView.findViewById(R.id.imgPoster);
        holder.nameOrTitle = (TextView) rowView.findViewById(R.id.name_or_title);
        holder.mediaType = (TextView) rowView.findViewById(R.id.mediaType);
        holder.characterOrJob = (TextView) rowView.findViewById(R.id.character_or_job);
        holder.characterOrJobHeading = (TextView) rowView.findViewById(R.id.character_or_job_heading);
        holder.epCountOrDept = (TextView) rowView.findViewById(R.id.epCount_or_dept);
        holder.epCountOrDeptHeading = (TextView) rowView.findViewById(R.id.epCount_or_dept_heading);
        holder.date = (TextView) rowView.findViewById(R.id.date);
        holder.dateHeading = (TextView) rowView.findViewById(R.id.dateHeading);

        Picasso.with(personview).load(roles.get(position).getPoster()).placeholder(R.drawable.movies).fit().into(holder.rolePoster);
        holder.mediaType.setText(roles.get(position).getMediaType());

        if(roles.get(position).getName() != "" && roles.get(position).getName() != null){
            holder.nameOrTitle.setText(roles.get(position).getName());
        }
        if(roles.get(position).getTitle() != "" && roles.get(position).getTitle() != null){
            holder.nameOrTitle.setText(roles.get(position).getTitle());
        }
        if(roles.get(position).getCharacter() != "" && roles.get(position).getCharacter() != null){
            holder.characterOrJobHeading.setText("Character :");
            holder.characterOrJob.setText(roles.get(position).getCharacter());
        }
        if (roles.get(position).getJob() != "" && roles.get(position).getJob() != null){
            holder.characterOrJobHeading.setText("Job :");
            holder.characterOrJob.setText(roles.get(position).getJob());
        }
        if(roles.get(position).getEpisodeCount() != "" && roles.get(position).getEpisodeCount() != null){
            holder.epCountOrDeptHeading.setText("Episode Count :");
            holder.epCountOrDept.setText(roles.get(position).getEpisodeCount());
        }
        if (roles.get(position).getDepartment() != "" && roles.get(position).getDepartment() != null){
            holder.epCountOrDeptHeading.setText("Department :");
            holder.epCountOrDept.setText(roles.get(position).getDepartment());
        }
        if(roles.get(position).getReleaseDate() != "" && roles.get(position).getReleaseDate() != null){
            holder.dateHeading.setText("Release Date :");
            holder.date.setText(roles.get(position).getReleaseDate());
        }
        if (roles.get(position).getFirstAirDate() != "" && roles.get(position).getFirstAirDate() != null){
            holder.dateHeading.setText("First Air Date :");
            holder.date.setText(roles.get(position).getFirstAirDate());
        }


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo: possibly add link to movie/ tv show role was in

//                Intent intent = new Intent(v.getContext(), ViewMovie.class);
//                intent.putExtra("passedMovieID", movies.get(position).getId());
//                v.getContext().startActivity(intent);
            }
        });


        return rowView;
    }
}
