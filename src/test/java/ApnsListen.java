import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.relayrides.*;
import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.RejectedNotificationReason;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;

public class ApnsListen {

	/**
	 * @param args
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws MalformedTokenStringException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws UnrecoverableKeyException,
			KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			MalformedTokenStringException, InterruptedException {
		// TODO Auto-generated method stub
		final PushManager<SimpleApnsPushNotification> pushManager = new PushManager<SimpleApnsPushNotification>(
				ApnsEnvironment.getSandboxEnvironment(),
				SSLContextUtil
						.createDefaultSSLContext(
								"/Users/towne/Documents/feiniu pusher/src/test/resources/app develop ios push RTMarket.p12",
								"123"), null, // Optional: custom event loop
												// group
				null, // Optional: custom ExecutorService for calling listeners
				null, // Optional: custom BlockingQueue implementation
				new PushManagerConfiguration(), "ExamplePushManager");
		
		pushManager.registerRejectedNotificationListener(new MyRejectedNotificationListener());

		pushManager.start();

		final byte[] token = TokenUtil
				.tokenStringToByteArray("<efeb40a4 b0bfb6db 8f9e4272 f7bb1a8f b50f79e1 f6c23d27 92fe56bb 3f60a125>");

		final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

		payloadBuilder.setAlertBody("我是PD区波www2fn://OpenUrl?url=http://www.feiniu.com/edm_mobile_app/20141222C0000001");
		payloadBuilder.setSoundFileName("ring-ring.aiff");

		final String payload = payloadBuilder.buildWithDefaultMaximumLength();

		pushManager.getQueue().put(
				new SimpleApnsPushNotification(token, payload));
		

	}

}

class MyRejectedNotificationListener implements RejectedNotificationListener<SimpleApnsPushNotification> {

    @Override
    public void handleRejectedNotification(
            final PushManager<? extends SimpleApnsPushNotification> pushManager,
            final SimpleApnsPushNotification notification,
            final RejectedNotificationReason reason) {

        System.out.format("%s was rejected with rejection reason %s\n", notification, reason);
    }
}
