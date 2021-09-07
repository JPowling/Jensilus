package de.jensilus.addresses.applications

import de.jensilus.components.subcomponents.NetworkInterface
import de.jensilus.launch

class DHCPServer(var networkInterface: NetworkInterface){
    private var isActive = false

    fun startServer(){
        initServer()
        isActive = true
        launch()
    }


    private fun initServer(){

    }

    private fun launch(){
        launch {
            while(true){

            }
        }
    }

    private fun listen(){

    }
}
