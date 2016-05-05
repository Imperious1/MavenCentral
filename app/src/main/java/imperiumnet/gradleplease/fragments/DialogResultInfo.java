package imperiumnet.gradleplease.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.IOException;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.adapters.RecyclerAdapter;
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.network.NetworkUtilsJson;
import imperiumnet.gradleplease.utils.Constant;

public class DialogResultInfo extends DialogFragment {

    Listeners.DialogClickListeners mDialogClickListener;
    private TextView mDescription;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDialogClickListener = (Listeners.DialogClickListeners) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_result_info, container);
        mDescription = (TextView) view.findViewById(R.id.frag_result_description);
        TextView mTitle = (TextView) view.findViewById(R.id.frag_result_title);
        TextView mGradleDependency = (TextView) view.findViewById(R.id.frag_result_gradle_dependency);
        TextView mMavenDependency = (TextView) view.findViewById(R.id.frag_result_maven_dependency);
        Button mExit = (Button) view.findViewById(R.id.exit_result);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogClickListener.hideFrag(true);
            }
        });
        String[] erData4now = RecyclerAdapter.getSearchList().get(RecyclerAdapter.getItemPos()).getLibrary().split(":");
        String mLatestVersion = RecyclerAdapter.getSearchList().get(RecyclerAdapter.getItemPos()).getLatestVersion();
        mGradleDependency.setText(String.format("dependencies { \n  compile '%s%s:%s' \n}", erData4now[0], erData4now[1], mLatestVersion));
        mMavenDependency.setText(String.format("<dependency> \n <groupId>%s</groupId>\n <artifactId>%s</artifactId>\n <version>%s</version\n</dependency>", erData4now[0], erData4now[1], mLatestVersion));
        mTitle.setText(String.format("%s%s:%s", erData4now[0], erData4now[1], mLatestVersion));
        new NetworkUtilsJson(new Listeners.TaskFinishedListener() {
            @Override
            public void processFinish(String output) throws JSONException, ParseException, IOException, SAXException {
                mDescription.setText(output);
            }
        }).execute(Constant.CONFIRM, "https://search.maven.org/remotecontent?filepath=" + erData4now[0]
                .replace(".", "/")
                .replace("[", "")
                .replace("]", "")
                .replace(":", "/") + "/" + erData4now[1] + "/" + mLatestVersion + "/" + erData4now[1] + "-" + mLatestVersion + ".pom");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
