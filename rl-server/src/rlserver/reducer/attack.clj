(ns rlserver.reducer.attack
  (:require [rlserver.system.action :refer [spend-ap]]
            [rllib.board :refer [move]]
            [rllib.selector :refer [get-entities-at]]
            [rlserver.system.resource :refer [replenish spend]]
            [rlserver.generate.effect :refer [generate-effect]]))

(defn pass-damage [state attackerid targetid]
  (let [pos (get-in state [:pos targetid])
        dmg (get-in state [:dmg attackerid])]
    (-> state
        (generate-effect pos 4)
        (update-in [:hp targetid] spend dmg)
        (spend-ap attackerid 1))))

(defn block-damage [state attackerid targetid]
  (let [pos (get-in state [:pos targetid])
        dmg (get-in state [:dmg attackerid])]
    (-> state
        (generate-effect pos 5)
        (update-in [:ap targetid] spend dmg)
        (spend-ap attackerid 1))))

(defn can-defend? [state targetid dmg]
  (and (some? (get-in state [:ap targetid 0]))
       (>= (get-in state [:ap targetid 0]) dmg)))

(defn entity-attack [state attackerid dir]
  (let [pos (get-in state [:pos attackerid])
        targetid (first (get-entities-at state (move pos dir) :hp))
        dmg (get-in state [:dmg attackerid])]
    (cond (nil? targetid) state
          (can-defend? state targetid dmg) (block-damage state attackerid targetid)
          true (pass-damage state attackerid targetid))))
