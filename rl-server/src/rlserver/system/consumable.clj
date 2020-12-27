(ns rlserver.system.consumable
  (:require
    [rllib.selector :refer [get-entities-at]]
    [rlserver.generate.object :refer [generate-corpse]]
    [rlserver.system.resource :refer [replenish]]
    [rlserver.entity.entity :refer [destroy-entity]]
    [rlserver.entity.state :refer [apply-seq]]))

(def effects {:buff (fn [s uid] (-> s
                                    (update-in [:hp uid 0] inc)
                                    (update-in [:hp uid 1] inc)))
              :heal (fn [s uid] (update-in s [:hp uid] replenish 3))})

(defn check-consumable [state uid]
  (let [pos (get-in state [:pos uid])
        id (first (get-entities-at state pos :consumable))]
    (if (nil? id) state
                  (let [eid (get-in state [:consumable id])
                        effect (get effects eid)]
                    (-> state
                        (destroy-entity id)
                        (effect uid))))))

(defn update-consumables [game]
  (apply-seq game (:pc game) check-consumable))