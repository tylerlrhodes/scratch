(ns scratch.core
  (:gen-class)
  (:require [scratch.import-static :as is])
  (:import (java.awt Color Dimension)
           (javax.swing JPanel JFrame Timer)
           (java.awt.event ActionListener KeyListener WindowEvent)))

(is/import-static java.awt.event.KeyEvent VK_LEFT VK_RIGHT VK_UP VK_DOWN)

(def game-infra nil)
(def rows 20)
(def cols 10)
(def board (atom (vec (repeat rows (vec (repeat cols 0))))))
(def piece (atom {:type 0 :position [0 0] :orientation 0}))

(def offset 20)
(def piece-width 20)
(def padding 2)
(def board-width (* (+ piece-width padding) cols))
(def board-height (* (+ piece-width padding) rows))
(def game-width (+ (* offset 2) (+ board-width (* (+ piece-width padding) 4) (* padding 4))))
(def game-height (+ (* offset 2) board-height))
(def bg-color Color/gray)

(defn paint-board [g]
  (.translate g offset offset)
  (doseq [x (range cols)
          y (range rows)]
      (.drawRect g (* x (+ padding piece-width)) (* y (+ padding piece-width)) piece-width piece-width)))

(defn update-board []
  nil)
  
(defn game-panel [frame]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      ;(. g drawString "Hi There" 10 10)
      (paint-board g))
    (actionPerformed [e]
      (update-board)
      (.repaint this))
    (keyPressed [e]
      (println e))
    (keyReleased [e])
    (keyTyped [e])
    (getPreferredSize []
      (Dimension. game-width game-height))))

(defn game []
  (let [frame (JFrame. "Test")
        panel (game-panel frame)
        timer (Timer. 1000 panel)]
    (doto panel
      (.setFocusable true)
      (.setBackground bg-color)
      (.addKeyListener panel))
    (doto frame
      (.add panel)
      (.pack)
      (.setResizable false)
      (.setVisible true))
    (.start timer)
    [frame panel timer]))

(defn start-game []
  (let [[frame panel timer] (game)]
    (def game-infra {:timer timer :frame frame :panel panel})))
    
(defn stop-game []
  (.stop (:timer game-infra))
  (.dispatchEvent (:frame game-infra) (WindowEvent. (:frame game-infra) WindowEvent/WINDOW_CLOSING)))

(defn -main []
  (game))

                      