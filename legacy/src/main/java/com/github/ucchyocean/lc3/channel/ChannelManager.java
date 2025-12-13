/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.channel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatAPI;
import com.github.ucchyocean.lc3.Messages;
import com.github.ucchyocean.lc3.event.EventResult;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.util.YamlConfig;

/**
 * チャンネルマネージャー
 * @author ucchy
 */
public class ChannelManager implements LunaChatAPI {

    private static final String FILE_NAME_DCHANNELS = "defaults.yml";
    private static final String FILE_NAME_JAPANIZE = "japanize.yml";
    private static final String FILE_NAME_DICTIONARY = "dictionary.yml";

    private File fileDefaults;
    private File fileJapanize;
    private File fileDictionary;
    private HashMap<String, Channel> channels;
    private HashMap<String, String> defaultChannels;
    private HashMap<String, Boolean> japanize;
    private java.util.LinkedHashMap<String, String> dictionary; // okocraft - Ensure that longer words are replaced first

    /**
     * コンストラクタ
     */
    public ChannelManager() {
        reloadAllData();
    }

    /**
     * すべて読み込みする
     */
    @Override
    public void reloadAllData() {

        // デフォルトチャンネル設定のロード
        fileDefaults = new File(LunaChat.getDataFolder(), FILE_NAME_DCHANNELS);

        if ( !fileDefaults.exists() ) {
            makeEmptyFile(fileDefaults);
        }

        YamlConfig config = YamlConfig.load(fileDefaults);

        defaultChannels = new HashMap<String, String>();
        for ( String key : config.getKeys(false) ) {
            String value = config.getString(key);
            if ( value != null) {
                defaultChannels.put(key, value.toLowerCase());
            }
        }

        // Japanize設定のロード
        fileJapanize = new File(LunaChat.getDataFolder(), FILE_NAME_JAPANIZE);

        if ( !fileJapanize.exists() ) {
            makeEmptyFile(fileJapanize);
        }

        YamlConfig configJapanize = YamlConfig.load(fileJapanize);

        japanize = new HashMap<String, Boolean>();
        for ( String key : configJapanize.getKeys(false) ) {
            japanize.put(key, configJapanize.getBoolean(key));
        }

        // dictionaryのロード
        fileDictionary = new File(LunaChat.getDataFolder(), FILE_NAME_DICTIONARY);

        if ( !fileDictionary.exists() ) {
            makeEmptyFile(fileDictionary);
        }

        YamlConfig configDictionary = YamlConfig.load(fileDictionary);

        dictionary = new java.util.LinkedHashMap<>(); // okocraft - Ensure that longer words are replaced first
        for ( String key : configDictionary.getKeys(false) ) {
            dictionary.put(key, configDictionary.getString(key));
        }
        net.okocraft.lunachat.japanize.Japanizer.sortDictionary(dictionary); // okocraft - Ensure that longer words are replaced first

        // チャンネル設定のロード
        channels = Channel.loadAllChannels();
    }

    /**
     * すべて保存する
     */
    protected void saveAllChannels() {

        saveDefaults();

        for ( Channel channel : channels.values() ) {
            channel.save();
        }
    }

    /**
     * デフォルトチャンネル設定を保存する
     * @return 保存したかどうか
     */
    private boolean saveDefaults() {
        // okocraft start - Make file saving async
        LunaChat.runAsyncTask(this::saveDefaults0);
        return true;
    }
    private boolean saveDefaults0() {
        // okocraft end
        try {
            net.okocraft.lunachat.DataFiles.saveStringMap(fileDefaults.toPath(), java.util.Map.copyOf(defaultChannels)); // okocraft - Make file saving async
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Japanize設定を保存する
     * @return 保存したかどうか
     */
    private boolean saveJapanize() {
        // okocraft start - Make file saving async
        LunaChat.runAsyncTask(this::saveJapanize0);
        return true;
    }
    private boolean saveJapanize0() {
        // okocraft end
        try {
            net.okocraft.lunachat.DataFiles.saveStringMap(fileJapanize.toPath(), com.google.common.collect.Maps.transformValues(java.util.Map.copyOf(japanize), bool -> Boolean.toString(bool))); // okocraft - Make file saving async
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Dictionary設定を保存する
     * @return 保存したかどうか
     */
    private boolean saveDictionary() {
        // okocraft start - Make file saving async
        LunaChat.runAsyncTask(this::saveDictionary0);
        return true;
    }
    private boolean saveDictionary0() {
        // okocraft end
        try {
            net.okocraft.lunachat.DataFiles.saveStringMap(fileDictionary.toPath(), java.util.Map.copyOf(dictionary)); // okocraft - Make file saving async
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * デフォルトチャンネル設定を全て削除する
     */
    public void removeAllDefaultChannels() {
        defaultChannels.clear();
        saveDefaults();
    }

    /**
     * プレイヤーのJapanize設定を返す
     * @param playerName プレイヤー名
     * @return Japanize設定
     */
    @Override
    public boolean isPlayerJapanize(String playerName) {
        if ( !japanize.containsKey(playerName) ) {
            return isUsingJapanese(playerName) && LunaChat.getConfig().isJapanizePlayerDefault(); // okocraft - Set the default setting of japanize to false for those who do not use Japanese as a client language
        }
        return japanize.get(playerName);
    }
    // okocraft start - Set the default setting of japanize to false for those who do not use Japanese as a client language
    private boolean isUsingJapanese(String playerName) {
        return com.github.ucchyocean.lc3.LunaChatVelocity.getInstance().isUsingJapanese(playerName);
    }
    // okocraft end

    /**
     * 指定したチャンネル名が存在するかどうかを返す
     * @param channelName チャンネル名
     * @return 存在するかどうか
     * @see LunaChatAPI#isExistChannel(String)
     */
    @Override
    public boolean isExistChannel(String channelName) {
        if ( channelName == null ) {
            return false;
        }
        return channels.containsKey(channelName.toLowerCase());
    }

    /**
     * 全てのチャンネルを返す
     * @return 全てのチャンネル
     * @see LunaChatAPI#getChannels()
     */
    @Override
    public Collection<Channel> getChannels() {

        return channels.values();
    }

    /**
     * プレイヤーが参加しているチャンネルを返す
     * @param playerName プレイヤー名
     * @return チャンネル
     * @see LunaChatAPI#getChannelsByPlayer(String)
     */
    @Override
    public Collection<Channel> getChannelsByPlayer(String playerName) {

        ChannelMember cp = ChannelMember.getChannelMember(playerName);
        Collection<Channel> result = new ArrayList<Channel>();
        for ( String key : channels.keySet() ) {
            Channel channel = channels.get(key);
            if ( channel.getMembers().contains(cp) ||
                    channel.isGlobalChannel() ) {
                result.add(channel);
            }
        }
        return result;
    }

    /**
     * プレイヤーが参加しているデフォルトのチャンネルを返す
     * @param playerName プレイヤー
     * @return チャンネル
     * @see LunaChatAPI#getDefaultChannel(String)
     */
    @Override
    public Channel getDefaultChannel(String playerName) {

        String cname = defaultChannels.get(playerName);

        if ( cname == null || !isExistChannel(cname) ) {
            return null;
        }
        return channels.get(cname);
    }

    /**
     * プレイヤーのデフォルトチャンネルを設定する
     * @param playerName プレイヤー
     * @param channelName チャンネル名
     * @see LunaChatAPI#setDefaultChannel(String, String)
     */
    @Override
    public void setDefaultChannel(String playerName, String channelName) {
        if ( channelName == null ) {
            removeDefaultChannel(playerName);
            return;
        }
        defaultChannels.put(playerName, channelName.toLowerCase());
        saveDefaults();
    }

    /**
     * 指定した名前のプレイヤーに設定されている、デフォルトチャンネルを削除する
     * @param playerName プレイヤー名
     * @see LunaChatAPI#removeDefaultChannel(String)
     */
    @Override
    public void removeDefaultChannel(String playerName) {
        if ( defaultChannels.containsKey(playerName) ) {
            defaultChannels.remove(playerName);
        }
        saveDefaults();
    }

    /**
     * チャンネルを取得する
     * @param channelName チャンネル名、または、チャンネルの別名
     * @return チャンネル
     * @see LunaChatAPI#getChannel(String)
     */
    @Override
    public Channel getChannel(String channelName) {
        if ( channelName == null ) return null;
        Channel channel = channels.get(channelName.toLowerCase());
        if ( channel != null ) return channel;
        for ( Channel ch : channels.values() ) {
            String alias = ch.getAlias();
            if ( alias != null && alias.length() > 0
                    && channelName.equalsIgnoreCase(ch.getAlias()) ) {
                return ch;
            }
        }
        return null;
    }

    /**
     * 新しいチャンネルを作成する
     * @param channelName チャンネル名
     * @return 作成されたチャンネル
     * @see LunaChatAPI#createChannel(String)
     */
    @Override
    public Channel createChannel(String channelName) {
        return createChannel(channelName, null);
    }

    /**
     * 新しいチャンネルを作成する
     * @param channelName チャンネル名
     * @param member チャンネルを作成した人
     * @return 作成されたチャンネル
     * @see LunaChatAPI#createChannel(String, ChannelMember)
     */
    @Override
    public Channel createChannel(String channelName, ChannelMember member) {

        // LunaChatChannelCreateEvent イベントコール
        EventResult result = LunaChat.getEventSender().sendLunaChatChannelCreateEvent(channelName, member);
        if ( result.isCancelled() ) {
            return null;
        }
        String name = result.getChannelName();

        Channel channel = new VelocityChannel(name);

        channels.put(name.toLowerCase(), channel);
        channel.save();
        return channel;
    }

    /**
     * チャンネルを削除する
     * @param channelName 削除するチャンネル名
     * @return 削除したかどうか
     * @see LunaChatAPI#removeChannel(String)
     */
    @Override
    public boolean removeChannel(String channelName) {
        return removeChannel(channelName, null);
    }

    /**
     * チャンネルを削除する
     * @param channelName 削除するチャンネル名
     * @param member チャンネルを削除した人
     * @return 削除したかどうか
     * @see LunaChatAPI#removeChannel(String, ChannelMember)
     */
    @Override
    public boolean removeChannel(String channelName, ChannelMember member) {

        channelName = channelName.toLowerCase();

        // LunaChatChannelRemoveEvent イベントコール
        EventResult result = LunaChat.getEventSender().sendLunaChatChannelRemoveEvent(channelName, member);
        if ( result.isCancelled() ) {
            return false;
        }
        channelName = result.getChannelName();

        Channel channel = getChannel(channelName);
        if ( channel != null ) {

            // 強制解散のメッセージを、残ったメンバーに流す
            String message = Messages.breakupMessage(channel.getColorCode(), channel.getName());
            if ( !channel.isPersonalChat() && !message.equals("") ) {
                for ( ChannelMember cp : channel.getMembers() ) {
                    cp.sendMessage(message);
                }
            }

            // チャンネルの削除
            channel.remove();
            channels.remove(channelName);
        }

        return true;
    }

    /**
     * 辞書データを全て取得する
     * @return 辞書データ
     */
    public HashMap<String, String> getAllDictionary() {
        return dictionary;
    }

    /**
     * 新しい辞書データを追加する
     * @param key キー
     * @param value 値
     */
    public void setDictionary(String key, String value) {
        dictionary.put(key, value);
        net.okocraft.lunachat.japanize.Japanizer.sortDictionary(dictionary); // okocraft - Ensure that longer words are replaced first
        saveDictionary();
    }

    /**
     * 指定したキーの辞書データを削除する
     * @param key キー
     */
    public void removeDictionary(String key) {
        dictionary.remove(key);
        net.okocraft.lunachat.japanize.Japanizer.sortDictionary(dictionary); // okocraft - Ensure that longer words are replaced first
        saveDictionary();
    }

    /**
     * 該当プレイヤーのJapanize変換をオン/オフする
     * @param playerName 設定するプレイヤー名
     * @param doJapanize Japanize変換するかどうか
     */
    @Override
    public void setPlayersJapanize(String playerName, boolean doJapanize) {
        japanize.put(playerName, doJapanize);
        saveJapanize();
    }

    /**
     * ChannelMemberのリストを、IDのStringリストに変換して返す
     * @param players
     * @return
     */
    private List<String> getIdList(List<ChannelMember> players) {
        List<String> results = new ArrayList<String>();
        for ( ChannelMember cp : players ) {
            results.add(cp.toString());
        }
        return results;
    }

    /**
     * 指定されたファイル出力先に、空のYamlファイルを作成する
     * @param file 出力先
     */
    private void makeEmptyFile(File file) {
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        YamlConfig config = new YamlConfig();
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
