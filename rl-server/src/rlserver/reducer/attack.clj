(ns rlserver.reducer.attack
  (:require [rlserver.system.action :refer [spend-ap]]
            [rllib.board :refer [move]]
            [rllib.rand :refer [rand-bool]]
            [rllib.selector :refer [get-entities-at]]
            [rlserver.system.resource :refer [replenish spend]]
            [rlserver.generate.timer :refer [generate-animated-effect]]))

(defn pass-damage [state attackerid targetid]
  (let [pos (get-in state [:pos targetid])
        dmg (get-in state [:dmg attackerid])]
    (-> state
        (generate-animated-effect pos 6)
        (update-in [:hp targetid] spend dmg))))

(defn block-damage [state attackerid targetid]
  (let [pos (get-in state [:pos targetid])
        dmg (get-in state [:dmg attackerid])]
    (-> state
        (generate-animated-effect pos 7)
        (update-in [:hp targetid] spend (int (/ dmg 2))))))

(defn entity-attack [state attackerid dir]
  (let [pos (get-in state [:pos attackerid])
        targetid (first (get-entities-at state (move pos dir) :hp))
        can-defend? #(rand-nth [true false])]
    (cond (nil? targetid) state
          (can-defend?) (block-damage state attackerid targetid)
          true (pass-damage state attackerid targetid))))
