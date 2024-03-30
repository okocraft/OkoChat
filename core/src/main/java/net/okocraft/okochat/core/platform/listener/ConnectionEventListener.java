package net.okocraft.okochat.core.platform.listener;

import java.util.UUID;

public abstract class ConnectionEventListener {

    protected void onJoin(UUID uuid, String name) {
        // TODO:
        // - UUIDをキャッシュ
        // - 強制参加チャンネル設定を確認し、参加させる (forceJoinToForceJoinChannels)
        // - グローバルチャンネル設定がある場合 (tryJoinToGlobalChannel)
        // - チャンネルチャット情報を表示する
    }

    protected void onQuit(UUID uuid, String name) {
        // TODO:
        // お互いがオフラインになるPMチャンネルがある場合はチャンネルをクリアする
    }



}
