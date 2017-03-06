package spotmetrics.ui.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: May 1, 2016
 */
public abstract class AbstractActionListenerPanel extends JPanel implements ActionListener {

        private static final long serialVersionUID = 4126439368593343819L;
        
        private EventListenerList eventListenerList = new EventListenerList();
        private int eventId = 0;
        
        public AbstractActionListenerPanel() {
                super();
        }
        
        /**
         * <p>Returns a unique event id.</p>
         * 
         * @return int
         */
        protected int getEventID() {
                int id = 0;
                if((eventId + 1) < Integer.MAX_VALUE) {
                        id++;
                }
                else {
                        id = 1;
                }

                return id;
        }
        
        /**
         * <p>Fires an action event to all listeners on this class.</p>
         * 
         * @param actionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
                Object[] listeners = eventListenerList.getListenerList();
                for(int i = listeners.length - 2; i >= 0; i -= 2) {
                        if(listeners[i] == ActionListener.class) {
                                ((ActionListener) listeners[i + 1]).actionPerformed(actionEvent);
                        }
                }
        }
        
        /**
         * <p>Adds an action listener to this class.</p>
         * 
         * @param actionListener
         */
        public void addActionListener(ActionListener actionListener) {
                eventListenerList.add(ActionListener.class, actionListener);
        }
        
        /**
         * <p>Removes an action listener from this class.</p>
         * 
         * @param actionListener
         */
        public void removeActionListener(ActionListener actionListener) {
                eventListenerList.remove(ActionListener.class, actionListener);
        }
}
