(ns rlserver.loop
  (:require [ruiyun.tools.timer :as t]
            [rllib.constant :refer [TICSIZE]]
            [rlserver.system.update :as u]
            [rlserver.generate.level.level :refer [generate-level with-new-pcs]]
            [rlserver.entity.state :refer [INITSTATE serialize-diff game-store seq-store]]))

(def timers (atom {}))

(defn start-loop! [id broadcaster]
  (if (nil? (get @timers id))
    (let [timer (t/timer (str id))]
      (swap! timers assoc id timer)
      (t/run-task! #(u/update id broadcaster) :period TICSIZE :by timer))))

(defn stop-loop! [id]
  (t/cancel! (get @timers id))
  (swap! timers assoc id nil))

(defn initialize-game [id broadcaster]
    (let [state (-> INITSTATE
                    (generate-level 1 0)
                    (with-new-pcs))]
      (swap! game-store assoc id state)
      (start-loop! id broadcaster)))

(defn destroy-game [id]
  (swap! game-store dissoc id)
  (swap! game-store dissoc (dec id))
  (stop-loop! id))
