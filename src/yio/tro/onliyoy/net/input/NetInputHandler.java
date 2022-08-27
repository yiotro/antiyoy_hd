package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.NetMessage;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.HashMap;

public class NetInputHandler {

    NetRoot root;
    HashMap<NmType, AbstractNetInputReaction> map;
    ObjectPoolYio<NetMessage> poolMessages;


    public NetInputHandler(NetRoot root) {
        this.root = root;
        initPools();
        initMap();
    }


    private void initMap() {
        map = new HashMap<>();
        add(NmType.hello, new AnirHello());
        add(NmType.welcome, new AnirWelcome());
        add(NmType.protocol, new AnirProtocol());
        add(NmType.kicked, new AnirKicked());
        add(NmType.joined_match, new AnirJoinedMatch());
        add(NmType.match_lobby_participants, new AnirMatchLobbyParticipants());
        add(NmType.match_start_time, new AnirMatchStartTime());
        add(NmType.user_data, new AnirUserData());
        add(NmType.custom_matches_list, new AnirCustomMatchesList());
        add(NmType.unable_to_join_custom_match, new AnirUnableToJoinCustomMatch());
        add(NmType.on_kicked_from_custom_match, new AnirOnKickedFromCustomMatch());
        add(NmType.match_launched, new AnirMatchLaunched());
        add(NmType.event, new AnirEvent());
        add(NmType.sync, new AnirSync());
        add(NmType.match_finished, new AnirMatchFinished());
        add(NmType.update_match_battle_data, new AnirUpdateMatchBattleData());
        add(NmType.recently_launched_matches, new AnirRecentlyLaunchedMatches());
        add(NmType.unable_to_spectate, new AnirUnableToSpectate());
        add(NmType.joined_as_spectator, new AnirJoinedAsSpectator());
        add(NmType.match_chat, new AnirMatchChat());
        add(NmType.sho, new AnirSho());
        add(NmType.kicked_for_two_turn_skip, new AnirKickedForTwoTurnSkip());
        add(NmType.admin_info, new AnirAdminInfo());
        add(NmType.notify_about_shut_down, new AnirNotifyAboutShutDown());
        add(NmType.go_to_main_lobby, new AnirGoToMainLobby());
        add(NmType.message_from_admin, new AnirMessageFromAdmin());
        add(NmType.protocol_is_valid, new AnirProtocolIsValid());
        add(NmType.fail_to_login_google, new AnirFailToLoginGoogle());
        add(NmType.please_check_in, new AnirPleaseCheckIn());
        add(NmType.player_statistics, new AnirPlayerStatistics());
        add(NmType.moderator_data, new AnirModeratorData());
        add(NmType.unable_to_verify_user_level, new AnirUnableToVerifyUserLevel());
        add(NmType.user_level_to_check, new AnirUserLevelToCheck());
        add(NmType.upload_allowed, new AnirUploadAllowed());
        add(NmType.upload_prohibited, new AnirUploadProhibited());
        add(NmType.user_levels, new AnirUserLevels());
        add(NmType.cant_find_user_level, new AnirCantFindUserLevel());
        add(NmType.play_user_level, new AnirPlayUserLevel());
        add(NmType.cant_find_report, new AnirCantFindReport());
        add(NmType.go_check_report, new AnirGoCheckReport());
        add(NmType.list_of_moderators, new AnirListOfModerators());
        add(NmType.search_result, new AnirSearchResult());
        add(NmType.update_user_data, new AnirUpdateUserData());
        add(NmType.user_dossier, new AnirUserDossier());
        add(NmType.mod_actions, new AnirModActions());
        add(NmType.undo_last_action, new AnirUndoLastAction());
        add(NmType.rejoin_list, new AnirRejoinList());
        add(NmType.rejoined_match, new AnirRejoinedMatch());
        add(NmType.toast, new AnirToast());
        add(NmType.debug_events_list, new AnirDebugEventsList());
        add(NmType.admin_text_report, new AnirAdminTextReport());
        add(NmType.cant_find_renaming, new AnirCantFindRenaming());
        add(NmType.go_check_renaming, new AnirGoCheckRenaming());
        add(NmType.update_experience, new AnirUpdateExperience());
        add(NmType.search_ul_results, new AnirSearchUlResults());
        add(NmType.leaderboard, new AnirLeaderboard());
        add(NmType.shop_data, new AnirShopData());
        add(NmType.purchases_data, new AnirPurchasesData());
        add(NmType.update_customization_data, new AnirUpdateCustomizationData());
        add(NmType.match_phrase, new AnirMatchPhrase());
        add(NmType.billing_notify_consume, new AnirBillingNotifyConsume());
        add(NmType.notify_fish_received, new AnirNotifyFishReceived());
        add(NmType.verify_ready_for_match, new AnirVerifyReadyForMatch());
        add(NmType.replay, new AnirReplay());
        add(NmType.match_statistics, new AnirMatchStatistics());
        add(NmType.sound_alert, new AnirSoundAlert());
        add(NmType.next_fish_in, new AnirNextFishIn());
        add(NmType.debug_request_current_turn, new AnirDebugRequestCurrentTurn());
        add(NmType.mass_check_renamings, new AnirMassCheckRenamings());
    }


    private void add(NmType nmType, AbstractNetInputReaction abstractNetInputReaction) {
        map.put(nmType, abstractNetInputReaction);
    }


    private void initPools() {
        poolMessages = new ObjectPoolYio<NetMessage>() {
            @Override
            public NetMessage makeNewObject() {
                return new NetMessage();
            }
        };
    }


    public void processMessage(String inputValue) {
        NetMessage message = poolMessages.getNext();
        message.decode(inputValue);
        NmType type = message.type;
        if (type == null) {
            System.out.println("NetReactionsManager.processMessage: unable to decode message [" + inputValue + "]");
            poolMessages.add(message);
            return;
        }
        if (!map.containsKey(type)) {
            System.out.println("NetReactionsManager.processMessage: no reaction for " + type);
            poolMessages.add(message);
            return;
        }
        AbstractNetInputReaction reaction = map.get(type);
        reaction.perform(root, message);
        root.problemsDetector.onMessageProcessed();
        poolMessages.add(message);
    }
}
