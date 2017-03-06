package spotmetrics.analyzer.export;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Mar 22, 2016
 *
 */
public class NotInitializedException extends Exception {

        private static final long serialVersionUID = -8296204627681986749L;

        public NotInitializedException() {
                super();
        }

        public NotInitializedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
                super(arg0, arg1, arg2, arg3);
        }

        public NotInitializedException(String arg0, Throwable arg1) {
                super(arg0, arg1);
        }

        public NotInitializedException(String arg0) {
                super(arg0);
        }

        public NotInitializedException(Throwable arg0) {
                super(arg0);
        }
}
