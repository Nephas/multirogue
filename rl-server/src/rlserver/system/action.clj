(ns rlserver.system.action
  (:require [rlserver.system.resource :refer [replenish spend]]
            [rlserver.entity.state :refer [apply-seq]]))

(defn spend-ap [state amount]
  (update state :pap spend amount))

(defn active? [game]
  (pos? (get-in game [:pap 0])))