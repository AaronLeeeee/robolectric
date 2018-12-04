package org.robolectric.android.controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import org.robolectric.util.ReflectionHelpers;

/**
 * FragmentController provides low-level APIs to control fragment's lifecycle.
 *
 * <p>Using FragmentController directly from your tests is strongly discouraged. You have to call
 * all the lifecycle callback methods (create, start, ...) in the same manner as the Android
 * framework by yourself otherwise you'll see fidelity issues. Consider using
 * {@link androidx.fragment.app.testing.FragmentScenario} instead, which provides higher-level,
 * streamlined APIs to control the lifecycle and it works with instrumentation tests too.
 *
 * @param <F> a class of the fragment which is under control by this class.
 */
public class FragmentController<F extends Fragment> extends ComponentController<FragmentController<F>, F> {
  private final F fragment;
  private final ActivityController<? extends Activity> activityController;

  public static <F extends Fragment> FragmentController<F> of(F fragment) {
    return of(fragment, FragmentControllerActivity.class, null, null);
  }

  public static <F extends Fragment> FragmentController<F> of(F fragment, Class<? extends Activity> activityClass) {
    return of(fragment, activityClass, null, null);
  }

  public static <F extends Fragment> FragmentController<F> of(F fragment, Intent intent) {
    return new FragmentController<>(fragment, FragmentControllerActivity.class, intent);
  }

  public static <F extends Fragment> FragmentController<F> of(F fragment, Bundle arguments) {
    return new FragmentController<>(fragment, FragmentControllerActivity.class, arguments);
  }

  public static <F extends Fragment> FragmentController<F> of(F fragment, Intent intent, Bundle arguments) {
    return new FragmentController<>(fragment, FragmentControllerActivity.class, intent,
            arguments);
  }

  public static <F extends Fragment> FragmentController<F> of(F fragment, Class<? extends Activity> activityClass, Intent intent) {
    return new FragmentController<>(fragment, activityClass, intent);
  }

  public static <F extends Fragment> FragmentController<F> of(F fragment, Class<? extends Activity> activityClass, Bundle arguments) {
    return new FragmentController<>(fragment, activityClass, arguments);
  }

  public static <F extends Fragment> FragmentController<F> of(F fragment, Class<? extends Activity> activityClass,
                                                              Intent intent, Bundle arguments) {
    return new FragmentController<>(fragment, activityClass, intent, arguments);
  }

  private FragmentController(F fragment, Class<? extends Activity> activityClass, Intent intent) {
    this(fragment, activityClass, intent, null);
  }

  private FragmentController(F fragment, Class<? extends Activity> activityClass, Bundle arguments) {
    this(fragment, activityClass, null, arguments);
  }

  private FragmentController(F fragment, Class<? extends Activity> activityClass,
                             Intent intent, Bundle arguments) {
    super(fragment, intent);
    this.fragment = fragment;
    if (arguments != null) {
      this.fragment.setArguments(arguments);
    }
    this.activityController = ActivityController.of(ReflectionHelpers.callConstructor(activityClass), intent);
  }

  /**
   * Creates the activity with {@link Bundle} and adds the fragment to the view with ID {@code contentViewId}.
   */
  public FragmentController<F> create(final int contentViewId, final Bundle bundle) {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.create(bundle).get().getFragmentManager().beginTransaction().add(contentViewId, fragment).commit();
      }
    });
    return this;
  }

  /**
   * Creates the activity with {@link Bundle} and adds the fragment to it. Note that the fragment will be added to the view with ID 1.
   */
  public FragmentController<F> create(Bundle bundle) {
    return create(1, bundle);
  }

  @Override
  public FragmentController<F> create() {
    return create(null);
  }

  @Override
  public FragmentController<F> destroy() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.destroy();
      }
    });
    return this;
  }

  public FragmentController<F> start() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.start();
      }
    });
    return this;
  }

  public FragmentController<F> resume() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.resume();
      }
    });
    return this;
  }

  public FragmentController<F> pause() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.pause();
      }
    });
    return this;
  }

  public FragmentController<F> visible() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.visible();
      }
    });
    return this;
  }

  public FragmentController<F> stop() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.stop();
      }
    });
    return this;
  }

  public FragmentController<F> saveInstanceState(final Bundle outState) {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        activityController.saveInstanceState(outState);
      }
    });
    return this;
  }

  private static class FragmentControllerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      LinearLayout view = new LinearLayout(this);
      view.setId(1);

      setContentView(view);
    }
  }
}
