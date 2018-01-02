package com.anachat.chatsdk.internal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anachat.chatsdk.internal.model.InputTypeAddress;
import com.anachat.chatsdk.internal.model.InputTypeDate;
import com.anachat.chatsdk.internal.model.InputTypeEmail;
import com.anachat.chatsdk.internal.model.InputTypeLocation;
import com.anachat.chatsdk.internal.model.InputTypeNumeric;
import com.anachat.chatsdk.internal.model.InputTypePhone;
import com.anachat.chatsdk.internal.model.InputTypeText;
import com.anachat.chatsdk.internal.model.InputTypeTime;
import com.anachat.chatsdk.internal.model.Item;
import com.anachat.chatsdk.internal.model.Media;
import com.anachat.chatsdk.internal.model.Message;
import com.anachat.chatsdk.internal.model.MessageCarousel;
import com.anachat.chatsdk.internal.model.MessageInput;
import com.anachat.chatsdk.internal.model.MessageSimple;
import com.anachat.chatsdk.internal.model.Option;
import com.anachat.chatsdk.internal.model.inputdata.DateRange;
import com.anachat.chatsdk.internal.model.inputdata.DefaultLocation;
import com.anachat.chatsdk.internal.model.inputdata.InputTypeList;
import com.anachat.chatsdk.internal.model.inputdata.InputTypeMedia;
import com.anachat.chatsdk.internal.model.inputdata.TextInputAttr;
import com.anachat.chatsdk.internal.model.inputdata.TimeRange;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static DatabaseHelper databaseHelper = null;

    private static final String DATABASE_NAME = "nfchat.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Message, Integer> messageDao;
    private Dao<InputTypeMedia, Integer> inputTypeMediaDao;
    private Dao<Option, Integer> optionDao;
    private Dao<MessageInput, Integer> messageInputDao;
    private Dao<MessageCarousel, Integer> messageCarouselDao;
    private Dao<Item, Integer> messageItemDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /************************************************
     *onCreate called on first time install
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, Message.class);
            TableUtils.createTable(connectionSource, MessageSimple.class);
            TableUtils.createTable(connectionSource, MessageCarousel.class);
            TableUtils.createTable(connectionSource, MessageInput.class);
            TableUtils.createTable(connectionSource, Media.class);
            TableUtils.createTable(connectionSource, Item.class);
            TableUtils.createTable(connectionSource, Option.class);
            TableUtils.createTable(connectionSource, InputTypeText.class);
            TableUtils.createTable(connectionSource, TextInputAttr.class);
            TableUtils.createTable(connectionSource, InputTypeLocation.class);
            TableUtils.createTable(connectionSource, DefaultLocation.class);
            TableUtils.createTable(connectionSource, InputTypeDate.class);
            TableUtils.createTable(connectionSource, DateRange.class);
            TableUtils.createTable(connectionSource, InputTypeEmail.class);
            TableUtils.createTable(connectionSource, InputTypePhone.class);
            TableUtils.createTable(connectionSource, InputTypeTime.class);
            TableUtils.createTable(connectionSource, InputTypeNumeric.class);
            TableUtils.createTable(connectionSource, TimeRange.class);
            TableUtils.createTable(connectionSource, InputTypeAddress.class);
            TableUtils.createTable(connectionSource, InputTypeMedia.class);
            TableUtils.createTable(connectionSource, InputTypeList.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Message.class, true);
            TableUtils.dropTable(connectionSource, MessageSimple.class, true);
            TableUtils.dropTable(connectionSource, MessageCarousel.class, true);
            TableUtils.dropTable(connectionSource, MessageInput.class, true);
            TableUtils.dropTable(connectionSource, Media.class, true);
            TableUtils.dropTable(connectionSource, Item.class, true);
            TableUtils.dropTable(connectionSource, Option.class, true);
            TableUtils.dropTable(connectionSource, InputTypeText.class, true);
            TableUtils.dropTable(connectionSource, TextInputAttr.class, true);
            TableUtils.dropTable(connectionSource, InputTypeLocation.class, true);
            TableUtils.dropTable(connectionSource, DefaultLocation.class, true);
            TableUtils.dropTable(connectionSource, InputTypeDate.class, true);
            TableUtils.dropTable(connectionSource, DateRange.class, true);
            TableUtils.dropTable(connectionSource, InputTypeEmail.class, true);
            TableUtils.dropTable(connectionSource, InputTypePhone.class, true);
            TableUtils.dropTable(connectionSource, InputTypeTime.class, true);
            TableUtils.dropTable(connectionSource, InputTypeNumeric.class, true);
            TableUtils.dropTable(connectionSource, TimeRange.class, true);
            TableUtils.dropTable(connectionSource, InputTypeAddress.class, true);
            TableUtils.dropTable(connectionSource, InputTypeMedia.class, true);
            TableUtils.dropTable(connectionSource, InputTypeList.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    //    public Dao<TeacherDetails, Integer> getTeacherDao() throws SQLException {
//        if (teacherDao == null) {
//            teacherDao = getDao(TeacherDetails.class);
//        }
//        return teacherDao;
//    }

    public void clearData(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        onUpgrade(sqliteDatabase, connectionSource, DATABASE_VERSION, DATABASE_VERSION);
    }

    public Dao<Message, Integer> getMessageDao() throws SQLException {
        if (messageDao == null) {
            messageDao = getDao(Message.class);
        }
        return messageDao;
    }

    public Dao<InputTypeMedia, Integer> getInputTypeMediaDao() throws SQLException {
        if (inputTypeMediaDao == null) {
            inputTypeMediaDao = getDao(InputTypeMedia.class);
        }
        return inputTypeMediaDao;
    }

    public Dao<Option, Integer> getOptionsDao() throws SQLException {
        if (optionDao == null) {
            optionDao = getDao(Option.class);
        }
        return optionDao;
    }

    public Dao<MessageInput, Integer> getMessageInputDao() throws SQLException {
        if (messageInputDao == null) {
            messageInputDao = getDao(MessageInput.class);
        }
        return messageInputDao;
    }

    public Dao<MessageCarousel, Integer> getMessageCarouselDao() throws SQLException {
        if (messageCarouselDao == null) {
            messageCarouselDao = getDao(MessageCarousel.class);
        }
        return messageCarouselDao;
    }

    public Dao<Item, Integer> getItemDao() throws SQLException {
        if (messageItemDao == null) {
            messageItemDao = getDao(Item.class);
        }
        return messageItemDao;
    }

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }


}