package net.sharksystem.persons.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.SharkException;

import java.util.HashSet;
import java.util.Set;

class CertificateListContentAdapter extends
        RecyclerView.Adapter<CertificateListContentAdapter.MyViewHolder>
        implements View.OnClickListener /*, View.OnLongClickListener */ {

    private final Context ctx;
    private View.OnClickListener clickListener;
//    private View.OnLongClickListener longClickListener;

//    private Set<CharSequence> selectedUserIDs = new HashSet<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ownerName, signerName, identityAssurance, validUntil;

        public MyViewHolder(View view) {
            super(view);
            ownerName = view.findViewById(R.id.certificate_list_row_owner);
            signerName = view.findViewById(R.id.certificate_list_row_signer);
            identityAssurance = view.findViewById(R.id.person_list_row_identityAssurance);
            validUntil = view.findViewById(R.id.certificate_list_row_validUntil);

            view.setOnClickListener(clickListener);
//            view.setOnLongClickListener(longClickListener);
        }
    }

    public CertificateListContentAdapter(Context ctx) throws SharkException {
        Log.d(this.getLogStart(), "constructor");
        this.ctx = ctx;
        this.clickListener = this;
        //this.longClickListener = this;
    }

    @Override
    public CertificateListContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(this.getLogStart(), "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.certificate_list_row, parent, false);

        return new CertificateListContentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CertificateListContentAdapter.MyViewHolder holder, int position) {
        Log.d(this.getLogStart(), "onBindViewHolder with position: " + position);

        /*
        I assume a bug or more probably - I'm to dull to understand recycler view at all.
        Here it comes: this method is called even with position 0
        But that position is never displayed.

        Only happens if we have a toolbar on top of a recycler view, though.

        So: I'm going to fake it until I understand the problem
        Fix: When position 0 called - I return a dummy message

        the other calls are handled as they should but with a decreased position
         */

        // Dummy values
        holder.ownerName.setText("ownerName");
        holder.signerName.setText("signerName");
        holder.identityAssurance.setText("iA");
        holder.validUntil.setText("validUntil");

        holder.itemView.setTag(R.id.certificate_list_owner_tag, "ownerID");
        holder.itemView.setTag(R.id.certificate_list_signer_tag, "signerID");

        /*
        if(position == 0) return;

        // fake position - see comments above
        position--;

        try {
            holder.itemView.setTag("ownerName | signerName");

        } catch (SharkException e) {
            Toast.makeText(this.ctx, "error finding person information: ", Toast.LENGTH_SHORT).show();
            return;
        }*/
    }

    @Override
    public int getItemCount() {
        Log.d(this.getLogStart(), "called getItemCount");

        return 4;
    }

    /*
    @Override
    public boolean onLongClick(View view) {
        CharSequence userID = (CharSequence)view.getTag();
        Intent intent = new PersonIntent(this.ctx, userID, PersonEditActivity.class);

        this.ctx.startActivity(intent);

        return true;
    }
*/

    @Override
    public void onClick(View view) {
        CharSequence owner = (CharSequence)view.getTag(R.id.certificate_list_owner_tag);
        CharSequence signer = (CharSequence)view.getTag(R.id.certificate_list_signer_tag);

        PersonIntent personIntent =
                new PersonIntent(this.ctx, owner, signer, CertificateViewActivity.class);

        this.ctx.startActivity(personIntent);
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
