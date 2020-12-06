(ns rlserver.loop
  (:require [ruiyun.tools.timer :as t]
            [rlserver.system.update :as u]
            [rlserver.state :refer [init-state serialize-game store]]))

(def timers (atom {}))

(defn start-loop! [id broadcaster]
  (if (nil? (get @timers id))
    (let [timer (t/timer id)]
      (swap! timers assoc id timer)
      (t/run-task! #(u/update id broadcaster) :period 500 :by timer))))

(defn stop-loop! [id]
  (t/cancel! (get @timers id))
  (swap! timers assoc id nil))

(defn initialize-game [id broadcaster]
  (let [state (-> init-state)]
    (swap! store assoc id state)
    (start-loop! id broadcaster)))