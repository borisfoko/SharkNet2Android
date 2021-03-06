package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.util.DateTimeHelper;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.persons.CredentialMessage;

import java.io.IOException;

public class PersonAddReceivedCredentialsActivity extends PersonAppActivity {
    private CredentialMessage credential;

    /*
    public PersonAddReceivedCredentialsActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_add_received_credential_layout);

        Log.d(this.getLogStart(), "onCreate");
        this.credential = PersonsStorageAndroidComponent.getPersonsStorage().getReceivedCredential();
        Log.d(this.getLogStart(), "got credential: " + this.credential);

        TextView tv = this.findViewById(R.id.credentialDisplayName);
        tv.setText(credential.getOwnerName());

        tv = this.findViewById(R.id.credentialID);
        tv.setText(credential.getOwnerID());

        tv = this.findViewById(R.id.credentialValidSince);
        tv.setText("valid since: " + DateTimeHelper.long2DateString(credential.getValidSince()));

        tv = this.findViewById(R.id.credentialsControlNumber);
        tv.setText(CredentialMessage.sixDigitsToString(credential.getRandomInt()));
    }

    public void onAddClick(View v) {
        try {
            /* debugging code - I nearly run nuts..
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            keyStore.load(null);
            KeyStore.PrivateKeyEntry entry =
                    (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEYSTORE_OWNER_ALIAS, null);

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEYSTORE_OWNER_ALIAS, null);

            Signature s = Signature.getInstance(SN_ANDROID_DEFAULT_SIGNING_ALGORITHM);
            s.initSign(privateKey);
//            s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
            byte[] data = "Hello".getBytes();
            s.update(data);
            byte[] signature = s.sign();
*/
            // sign and create certificate
            Log.d(this.getLogStart(), "before call addAndSignPerson()");
            ASAPCertificate newCert = PersonsStorageAndroidComponent.getPersonsStorage().addAndSignPerson(
                    this.credential.getOwnerID(),
                    this.credential.getOwnerName(),
                    this.credential.getPublicKey(),
                    this.credential.getValidSince());

            // return newly created certificate
            Log.d(this.getLogStart(), "right before sending certificate as ASAP Message...");
            this.sendASAPMessage(ASAPCertificateStorage.CERTIFICATE_APP_NAME,
                    ASAPCertificate.ASAP_CERTIFICATE_URI,
                    newCert.asBytes(), true);
            Log.d(this.getLogStart(), ".. sent certificate message");

        } catch (ASAPException | IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.i(this.getLogStart(), e.getLocalizedMessage());
        } catch (RuntimeException e) {
            Log.d(this.getLogStart(), "fatal. Could not add and sign person: "
                    + e.getLocalizedMessage());
            String text = e.getLocalizedMessage(); // debug break.
/*        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | SignatureException | UnrecoverableEntryException | InvalidKeyException e) {
            Log.d(this.getLogStart(), "fatal: "
                    + e.getLocalizedMessage());
 */
        }
        this.finish();
    }
}
