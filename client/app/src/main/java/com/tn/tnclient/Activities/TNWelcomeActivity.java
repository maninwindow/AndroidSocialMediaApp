package com.tn.tnclient.Activities;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;
import com.tn.tnclient.R;

/**
 * Created by alimjan on 10/24/17.
 */

public class TNWelcomeActivity extends WelcomeActivity {

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
            .defaultBackgroundColor(R.color.colorPrimary)
            .page(new TitlePage(R.color.colorPrimary,
                    "Title")
            )
            .page(new BasicPage(R.color.wrong_field,
                    "Header",
                    "More text.")
                    .background(R.color.wrong_field)
            )
            .page(new BasicPage(R.color.grey,
                    "Third page",
                    "Here we are...")
            )
            .swipeToDismiss(true)
            .build();
    }

}
