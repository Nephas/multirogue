(ns rlclient.render.world
  (:require [rlclient.graphics.camera :as c]
            [quil.core :as q]
            [rlclient.graphics.sheets :as sh]
            [rllib.selector :refer [get-entities-at]]
            [rllib.vector :refer [add]]
            [rlclient.network.session :as r]
            [rlclient.graphics.gui.bar :as b]
            [rlclient.graphics.wall :as w]
            [rlclient.graphics.floor :as f]))

(defn get-text [state id]
  (let [title (get-in state [:desc id])
        items (->> [:hp :dmg :consumable]
                   (map #(if (some? (get-in state [% id])) (str % " " (get-in state [% id]))))
                   (filter some?)
                   (cons ""))]
    (str "~" title "~\n" (clojure.string/join "\n " items) "\n")))

(defn render-world! [state]
  (do (c/update-camera)
      (q/background 0)
      ;walls
      (doseq [[pos tile] (:tiles @w/wallmap)]
        (c/at-mappos (sh/get-tile tile) pos))

      ;floors
      (doseq [[pos tile] (:tiles @f/floormap)]
        (c/at-mappos (sh/get-tile tile) pos))

      ;sprites
      (doseq [[id tile] (:sprite state)]
        (c/at-mappos (sh/get-tile tile)
                     (get (:pos state) id)))

      ;animations
      (doseq [[id animation] (:animated state)]
        (c/at-mappos (sh/get-animation animation (hash id))
                     (get (:pos state) id)))

      (c/at-mappos (sh/get-tile [11 12]) @c/cursor)

      (q/fill 0)
      (q/rect 0 0 160 800)
      (q/fill 1.0)

      (q/text (get-in state [:desc (r/player-id)]) 30 100)
      (b/red-bar [1 1] (get-in state [:hp (r/player-id)]))
      (b/yellow-bar [1 2] [(get-in state [:dmg (r/player-id)])
                           (get-in state [:dmg (r/player-id)])])

      (q/text (str "Level: " (:level state)) 30 210)
      (q/text (str "Turn: " (:turn state) "|" (:tic state)) 30 240)
      (b/green-bar [1 11] (get state :pap))

      (q/text (get-in state [:desc (r/other-player)]) 30 460)
      (b/red-bar [1 16] (get-in state [:hp (r/other-player)]))
      (b/yellow-bar [1 17] [(get-in state [:dmg (r/other-player)])
                            (get-in state [:dmg (r/other-player)])])

      (let [[x y] (c/at-mappos (add @c/cursor [1 0]))
            ids (get-entities-at state @c/cursor)
            text (clojure.string/join "\n" (map #(get-text state %) ids))]
        (when-not (empty? ids)
          (q/fill 0)
          (q/rect x y 100 200)
          (q/fill 1.0)
          (q/text text x y 100 200)))))