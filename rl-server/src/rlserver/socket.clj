(ns rlserver.socket
  (:require [clojure.string :as str]
            [rlserver.entity.state :refer [game-store serialize-diff serialize-full]]
            [rlserver.loop :as l]
            [rlserver.reducer.player :refer [handle-action resolve-tic player-join player-disjoin]]
            [org.httpkit.server :as http]))

(def channels (atom {}))

(defn connected-players [gid]
  (keys (get @channels gid)))

(defn broadcast [gid msg]
  (doseq [ch (vals (get @channels gid))]
    (http/send! ch msg)))

(defn log-header [gid pid]
  (str "[gid: " gid " - pid: " pid "]"))

(defn handle [msg channel gid pid]
  (do (println (log-header gid pid) msg)
      (cond (str/includes? msg "disconnect")
            (do (swap! channels update gid dissoc pid)
                (println "\t * open channels:" (count (get @channels gid)))
                (swap! game-store update gid player-disjoin pid)
                (broadcast gid (serialize-diff gid))
                (when (empty? (get @channels gid))
                  (println "\t * closing game")
                  (l/destroy-game gid)))

            (str/includes? msg "reset")
            (do (println "\t * reset game")
                (l/preload-game gid)
                (broadcast gid (serialize-full gid))
                (l/initialize-game gid)
                (doseq [id (connected-players gid)]
                  (swap! game-store update gid player-join id))
                (broadcast gid (serialize-full gid)))

            (str/includes? msg "connect")
            (do (swap! channels assoc-in [gid pid] channel)
                (println "\t * open channels:" (count (get @channels gid)))
                (when (nil? (get @game-store gid))
                  (l/preload-game gid)
                  (broadcast gid (serialize-full gid))
                  (l/initialize-game gid))
                (swap! game-store update gid player-join pid)
                (broadcast gid (str "connect player " pid))
                (broadcast gid (serialize-full gid)))

            (str/includes? msg "action")
            (let [action (read-string (last (str/split msg #"action")))]
              (do (swap! game-store update gid handle-action action pid)
                  (swap! game-store update gid resolve-tic)
                  (println "\t * state tic:" (get-in @game-store [gid :tic]))
                  (broadcast gid (serialize-diff gid)))))))