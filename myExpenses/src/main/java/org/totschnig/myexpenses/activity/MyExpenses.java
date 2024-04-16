/*   This file is part of My Expenses.
 *   My Expenses is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   My Expenses is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with My Expenses.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.totschnig.myexpenses.activity;

import static org.totschnig.myexpenses.activity.ConstantsKt.EDIT_REQUEST;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ROWID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_TRANSACTIONID;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.dialog.ConfirmationDialogFragment.ConfirmationDialogListener;
import org.totschnig.myexpenses.dialog.HelpDialogFragment;
import org.totschnig.myexpenses.dialog.select.SelectFilterDialog;
import org.totschnig.myexpenses.dialog.select.SelectHiddenAccountDialogFragment;
import org.totschnig.myexpenses.model.ContribFeature;
import org.totschnig.myexpenses.util.AppDirHelper;
import org.totschnig.myexpenses.util.Utils;
import org.totschnig.myexpenses.util.ads.AdHandler;
import org.totschnig.myexpenses.util.crashreporting.CrashHandler;
import org.totschnig.myexpenses.util.distrib.DistributionHelper;

import eltos.simpledialogfragment.list.MenuDialog;

/**
 * This is the main activity where all expenses are listed
 * From the menu sub activities (Insert, Reset, SelectAccount, Help, Settings)
 * are called
 */
public class MyExpenses extends BaseMyExpenses implements
    ConfirmationDialogListener, SelectFilterDialog.Host {

  private AdHandler adHandler;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    adHandler = adHandlerFactory.create(binding.viewPagerMain.adContainer, this);
    binding.viewPagerMain.adContainer.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {

          @Override
          public void onGlobalLayout() {
            binding.viewPagerMain.adContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            adHandler.startBanner();
          }
        });

    try {
      adHandler.maybeRequestNewInterstitial();
    } catch (Exception e) {
      CrashHandler.report(e);
    }

    getNavigationView().setNavigationItemSelectedListener(item -> dispatchCommand(item.getItemId(), null));
    View navigationMenuView = getNavigationView().getChildAt(0);
    if (navigationMenuView != null) {
      navigationMenuView.setVerticalScrollBarEnabled(false);
    }
    updateFab();
    setupFabSubMenu();
    if (!isScanMode()) {
      getFloatingActionButton().setVisibility(View.INVISIBLE);
    }
    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if (extras != null) {
        long fromExtra = Utils.getFromExtra(extras, KEY_ROWID, 0);
        if (fromExtra != 0) {
          setSelectedAccountId(fromExtra);
        }
        showTransactionFromIntent(extras);
      }
    }

    if (savedInstanceState == null) {
      newVersionCheck();
      voteReminderCheck();
    }
    reviewManager.init(this);
  }

  public void showTransactionFromIntent(Bundle extras) {
    long idFromNotification = extras.getLong(KEY_TRANSACTIONID, 0);
    //detail fragment from notification should only be shown upon first instantiation from notification
    if (idFromNotification != 0) {
      showDetails(idFromNotification);
      getIntent().removeExtra(KEY_TRANSACTIONID);
    }
  }

  /**
   * Can be used to ask user who has already voted to update their vote. Currently not used
   */
/*  private void voteReminderCheck2() {
    roadmapViewModel.getShouldShowVoteReminder().observe(this, shouldShow -> {
      if (shouldShow) {
        prefHandler.putLong(PrefKey.VOTE_REMINDER_LAST_CHECK, System.currentTimeMillis());
        showSnackBar(getString(R.string.reminder_vote_update), Snackbar.LENGTH_INDEFINITE,
                new SnackbarAction(getString(R.string.vote_reminder_action), v -> {
          Intent intent = new Intent(this, RoadmapVoteActivity.class);
          startActivity(intent);
        }));
      }
    });
  }*/

  @Override
  protected void onActivityResult(int requestCode, int resultCode,
                                  Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (requestCode == EDIT_REQUEST) {
      getFloatingActionButton().show();
      if (resultCode == RESULT_OK) {
        if (!adHandler.onEditTransactionResult()) {
          reviewManager.onEditTransactionResult(this);
        }
      }
    }
  }

  /**
   * @return true if command has been handled
   */
  public boolean dispatchCommand(int command, Object tag) {
    if (super.dispatchCommand(command, tag)) {
      return true;
    }
    Intent i;
    if (command == R.id.BUDGET_COMMAND) {
      contribFeatureRequested(ContribFeature.BUDGET, null);
      return true;
    } else if (command == R.id.HELP_COMMAND_DRAWER) {
      i = new Intent(this, Help.class);
      i.putExtra(HelpDialogFragment.KEY_CONTEXT, "NavigationDrawer");
      startActivity(i);
      return true;
    } else if (command == R.id.MANAGE_TEMPLATES_COMMAND) {
      i = new Intent(this, ManageTemplates.class);
      startActivity(i);
      return true;
    } else if (command == R.id.SHARE_COMMAND) {
      i = new Intent();
      i.setAction(Intent.ACTION_SEND);
      i.putExtra(Intent.EXTRA_TEXT, Utils.getTellAFriendMessage(this).toString());
      i.setType("text/plain");
      startActivity(Intent.createChooser(i, getResources().getText(R.string.menu_share)));
      return true;
    } else if (command == R.id.CANCEL_CALLBACK_COMMAND) {
      finishActionMode();
      return true;
    } else if (command == R.id.OPEN_PDF_COMMAND) {
      i = new Intent();
      i.setAction(Intent.ACTION_VIEW);
      Uri data = AppDirHelper.ensureContentUri(Uri.parse((String) tag), this);
      i.setDataAndType(data, "application/pdf");
      i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      startActivity(i, R.string.no_app_handling_pdf_available, null);
      return true;
    } else if (command == R.id.SORT_COMMAND) {
      MenuDialog.build()
          .menu(this, R.menu.accounts_sort)
          .choiceIdPreset(accountSort.getCommandId())
          .title(R.string.display_options_sort_list_by)
          .show(this, DIALOG_TAG_SORTING);
      return true;
    } else if (command == R.id.ROADMAP_COMMAND) {
      Intent intent = new Intent(this, RoadmapVoteActivity.class);
      startActivity(intent);
      return true;
    } else if (command == R.id.HIDDEN_ACCOUNTS_COMMAND) {
      SelectHiddenAccountDialogFragment.newInstance().show(getSupportFragmentManager(),
          MANAGE_HIDDEN_FRAGMENT_TAG);
      return true;
    } else if (command == R.id.OCR_FAQ_COMMAND) {
      startActionView("https://github.com/mtotschnig/MyExpenses/wiki/FAQ:-OCR");
      return true;
    } else if (command == R.id.BACKUP_COMMAND) {
      i = new Intent(this, BackupRestoreActivity.class);
      i.setAction(BackupRestoreActivity.ACTION_BACKUP);
      startActivity(i);
    } else if (command == R.id.MANAGE_PARTIES_COMMAND) {
      i = new Intent(this, ManageParties.class);
      i.setAction(Action.MANAGE.name());
      startActivity(i);
    }
    return false;
  }

  @Override
  public void contribFeatureNotCalled(@NonNull ContribFeature feature) {
    if (!DistributionHelper.isGithub() && feature == ContribFeature.AD_FREE) {
      finish();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Bundle extras = intent.getExtras();
    if (extras != null) {
      setSelectedAccountId(extras.getLong(KEY_ROWID));
      showTransactionFromIntent(extras);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    adHandler.onResume();
  }

  @Override
  public void onDestroy() {
    adHandler.onDestroy();
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    adHandler.onPause();
    super.onPause();
  }

  public void onBackPressed() {
    if (binding.drawer != null && binding.drawer.isDrawerOpen(GravityCompat.START)) {
      binding.drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }
}