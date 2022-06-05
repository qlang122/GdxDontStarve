override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmReceiver = AlarmReceiver()
            val filter = IntentFilter(ALARM_EVENT)
            Log.e("QL", "------register alarm------->")
            registerReceiver(alarmReceiver, filter)
        }

    }

override fun onBackPressed() {
        startActivity(Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) })
    }

private fun setAlarm() {
//        val intent = PendingIntent.getBroadcast(this, 0, Intent(ALARM_EVENT), PendingIntent.FLAG_UPDATE_CURRENT)
        val intent = PendingIntent.getService(this, 0, Intent(this, MyService::class.java), PendingIntent.FLAG_NO_CREATE)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        alarmManager?.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR,
                intent
        )

//        when {
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
//                alarmManager?.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), intent)
//            }
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
//                alarmManager?.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), intent)
//            }
//            else -> {
//                alarmManager?.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
//                        AlarmManager.INTERVAL_FIFTEEN_MINUTES, intent)
//            }
//        }

        Toast.makeText(this, "设置完成", Toast.LENGTH_SHORT).show()
    }

private fun startWorker() {
        val workManager = WorkManager.getInstance(MyApp.instance)

        val constraints: Constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

        val workRequest = PeriodicWorkRequest.Builder(WakeupWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(WORK_TAG)
                .build()

        val operation = workManager.enqueue(workRequest)
        operation.state.observe(this) {
            Log.e("QL", "-----start work----->${it}")
        }
    }

    private fun stopWorker() {
        WorkManager.getInstance(MyApp.instance).cancelAllWorkByTag(WORK_TAG).state.observe(this) {
            Log.e("QL", "------stop work----->>$it")
        }
    }
    
    class WakeupWorker(val context: Context, params: WorkerParameters) : ListenableWorker(context, params) {
    private val ALARM_EVENT = "com.qlang.alarm.receiver"

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture {
            Log.e("QL", "------Work in time run------")
            context.sendBroadcast(Intent(ALARM_EVENT))
            it.set(Result.success())
        }
    }

    override fun onStopped() {
        Log.e("QL", "------Work on stop------")
    }
}
