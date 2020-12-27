(ns rlclient.input.action
  (:require [rlclient.network.connect :refer [remote-state]]
            [rllib.board :refer [move]]
            [rlclient.network.session :as s]))

(def actions {:w          [:move :up]
              :a          [:move :left]
              :s          [:move :down]
              :d          [:move :right]

              :ArrowUp    [:move :up]
              :ArrowLeft  [:move :left]
              :ArrowDown  [:move :down]
              :ArrowRight [:move :right]

              :1          [:item :1]
              :2          [:item :2]
              :3          [:item :3]

              :t          [:pass nil]
              :space      [:pass nil]})

(defn move-target [dir]
  (let [pos (get-in @remote-state [:pos (s/player-id)])]
    (move pos dir)))

(defn has-hp? [target-pos]
  (let [{entities :pos
         hp-comps :hp} @remote-state
        not-empty? #(not (empty? %))]
    (->> entities
         (filter (fn [[id pos]] (and (= pos target-pos) (contains? hp-comps id))))
         (not-empty?))))

(defn loading-screen? []
  (not (neg? (get @remote-state :load))))

(defn player-alive? []
  (> (get-in @remote-state [:hp (s/player-id) 0]) 0))

(defn context-action [key]
  (let [[primary secondary] (get actions key)]
    (cond
      (loading-screen?)
      (if (= :space key) [:continue nil] nil)

      (not (player-alive?))
      nil

      (and (= :move primary) (has-hp? (move-target secondary)))
      [:attack secondary]

      true [primary secondary])))