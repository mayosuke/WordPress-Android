package org.wordpress.android.ui.accounts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

import org.wordpress.android.R;
import org.wordpress.android.WordPress;
import org.wordpress.android.ui.AppLogViewerActivity;
import org.wordpress.android.util.ABTestingUtils;
import org.wordpress.android.util.ABTestingUtils.Feature;
import org.wordpress.android.util.HelpshiftHelper;
import org.wordpress.android.util.HelpshiftHelper.MetadataKey;
import org.wordpress.android.util.HelpshiftHelper.Tag;
import org.wordpress.android.widgets.WPTextView;

import java.util.ArrayList;

public class HelpActivity extends ActionBarActivity {
    final private static String FAQ_URL = "http://android.wordpress.org/faq/";
    final private static String FORUM_URL = "http://android.forums.wordpress.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ABTestingUtils.isFeatureEnabled(Feature.HELPSHIFT)) {
            initHelpshiftLayout();
        } else {
            initDefaultLayout();
        }

        // Init common elements
        WPTextView version = (WPTextView) findViewById(R.id.nux_help_version);
        version.setText(getString(R.string.version) + " " + WordPress.versionName);

        WPTextView applogButton = (WPTextView) findViewById(R.id.applog_button);
        applogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AppLogViewerActivity.class));
            }
        });
    }

    private void initHelpshiftLayout() {
        setContentView(R.layout.help_activity_with_helpshift);

        WPTextView version = (WPTextView) findViewById(R.id.nux_help_version);
        version.setText(getString(R.string.version) + " " + WordPress.versionName);
        WPTextView contactUsButton = (WPTextView) findViewById(R.id.contact_us_button);
        contactUsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                Tag origin = Tag.ORIGIN_UNKNOWN;
                if (extras != null) {
                    // This could be moved to WelcomeFragmentSignIn directly, but better to have all Helpshift
                    // related code at the same place (Note: value can be null).
                    HelpshiftHelper.getInstance().addMetaData(MetadataKey.USER_ENTERED_URL, extras.getString(
                            SignInFragment.ENTERED_URL_KEY));
                    HelpshiftHelper.getInstance().addMetaData(MetadataKey.USER_ENTERED_USERNAME, extras.getString(
                            SignInFragment.ENTERED_USERNAME_KEY));
                    origin = (Tag) extras.get(HelpshiftHelper.ORIGIN_KEY);
                }
                HelpshiftHelper.getInstance().showConversation(HelpActivity.this, origin);
            }
        });

        WPTextView faqbutton = (WPTextView) findViewById(R.id.faq_button);
        faqbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                Tag origin = Tag.ORIGIN_UNKNOWN;
                if (extras != null) {
                    origin = (Tag) extras.get(HelpshiftHelper.ORIGIN_KEY);
                }
                HelpshiftHelper.getInstance().showFAQ(HelpActivity.this, origin);
            }
        });
    }

    private void initDefaultLayout() {
        setContentView(R.layout.help_activity);

        WPTextView helpCenterButton = (WPTextView) findViewById(R.id.help_button);
        helpCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FAQ_URL)));
            }
        });

        WPTextView forumButton = (WPTextView) findViewById(R.id.forum_button);
        forumButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FORUM_URL)));
            }
        });
    }
}
