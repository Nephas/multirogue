(ns rlserver.system.consumable
  (:require
    [rllib.selector :refer [get-entities-at]]
    [rlserver.generate.object :refer [generate-corpse]]
            [rlserver.system.resource :refer [replenish]]
            [rlserver.entity.entity :refer [destroy-entity]]
            [rlserver.entity.state :refer [apply-seq]]))

(defn check-consumable [state id]
  (let [pos (get-in state [:pos id])
        consumableid (first (get-entities-at state pos :consumable))]
    (if (nil? consumableid) state
                            (-> state
                                (destroy-entity consumableid)
                                (update-in [:hp id] replenish 3)))))

(defn update-consumables [game]
  (apply-seq game (:pc game) check-consumable))