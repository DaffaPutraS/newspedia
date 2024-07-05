package com.example.newspedia;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.newspedia.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityInstrumentedTest {

    @Rule
    public IntentsTestRule<LoginActivity> intentsTestRule = new IntentsTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void setUpClass() {
        // Point to the Firebase Auth and Database emulators
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099);
        FirebaseDatabase.getInstance().useEmulator("10.0.2.2", 9000);

    }

    @Test
    public void loginWithValidCredentials_intentsToMainActivity() {
        String testEmail = "ngabroger@gmail.com";
        String testPassword = "testing123";

        onView(withId(R.id.emailRegisterInputText)).perform(typeText(testEmail));
        onView(withId(R.id.passRegisterInputText)).perform(typeText(testPassword));
        onView(withId(R.id.loginBtn)).perform(click());

        // Verify that an intent to MainActivity was sent

    }
}