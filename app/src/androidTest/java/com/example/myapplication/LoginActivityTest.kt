package com.example.myapplication


import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.myapplication.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    private val name = "asoe"
    private val email = "asoe@asoe.de"
    private val password = "garfield"

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource())
    }

    @Test
    fun test_Login() {

        try {
            onView(withId(R.id.login_main)).check(matches(isDisplayed()))
        } catch (ignore: NoMatchingViewException) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
            onView(withText("Log Out")).perform(click())
        }
        onView(withId(R.id.nameET)).perform(typeText(name))
        onView(withId(R.id.emailET)).perform(typeText(email))
        onView(withId(R.id.password1ET)).perform(typeText(password), ViewActions.pressKey(KeyEvent.KEYCODE_BACK))

        onView(withId(R.id.loginBt)).perform(click())

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()))
    }
}