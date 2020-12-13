(ns rlserver.system.consumable
  (:require [rlserver.generate.object :refer [generate-corpse]]
            [rlserver.system.resource :refer [replenish]]
            [rlserver.entity.entity :refer [destroy-entity]]
            [rlserver.entity.state :refer [apply-seq]]))

(defn get-consumables-at [state target-pos]
  (->> (get state :pos)
       (filter (fn [[id pos]] (and (= pos target-pos) (contains? (:consumable state) id))))
       (map (fn [[id pos]] id))))

(defn check-consumable [state id]
  (let [pos (get-in state [:pos id])
        consumableid (first (get-consumables-at state pos))]
    (if (nil? consumableid) state
                            (-> state
                                (destroy-entity consumableid)
                                (update-in [:hp id] replenish 3)))))

(defn update-consumables [game]
  (apply-seq game (:pc game) check-consumable))