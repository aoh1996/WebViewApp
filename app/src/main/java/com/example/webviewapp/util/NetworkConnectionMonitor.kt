import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import kotlin.math.log

private const val TAG = "monitor"

class NetworkConnectionMonitor (private val connectivityManager: ConnectivityManager) : LiveData<Boolean>() {

    private constructor(application: Application) : this(application.getSystemService(Context.CONNECTIVITY_SERVICE) as  ConnectivityManager)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(TAG, "onAvailable: true")
            postValue(true)
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d(TAG, "onLost: true")
            postValue(false)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d(TAG, "onUnavailable: true")
            postValue(false)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        Log.d(TAG, "onActive: called")
        super.onActive()
        val builder = NetworkRequest.Builder()
        postValue(false)
        connectivityManager.registerNetworkCallback(builder.build(),networkCallback)
    }

    override fun onInactive() {
        Log.d(TAG, "onInactive: called")
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    companion object {

        @Volatile private var INSTANCE: NetworkConnectionMonitor? = null

        fun getInstance(application: Application): NetworkConnectionMonitor {
            Log.d(TAG, "getInstance: called")
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkConnectionMonitor(application).also { INSTANCE = it }
            }
        }
    }

}