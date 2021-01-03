(ns rlserver.system.action
  (:require [rlserver.system.resource :refer [replenish spend]]))

(defn spend-ap [state amount]
  (update state :pap spend amount))

(defn active? [game]
  (pos? (get-in game [:pap 0])))