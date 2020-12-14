(ns rlserver.system.action
  (:require [rlserver.system.resource :refer [replenish spend]]
            [rlserver.entity.state :refer [apply-seq]]))

(defn spend-ap [state id amount]
  (if (some? (get-in state [:ap id]))
    (update-in state [:ap id] spend amount)
    state))

(defn replenish-ap [state id]
  (update-in state [:ap id] replenish 1))

(defn replenish-actions [game]
  (let [stats (keys (:ap game))]
    (apply-seq game stats replenish-ap)))

(defn active? [game id]
  (pos? (get-in game [:ap id 0])))