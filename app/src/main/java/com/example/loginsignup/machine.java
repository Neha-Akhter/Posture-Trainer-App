package com.example.loginsignup;

public class machine {
    private String machineName;
    private String machineAddress;

    public machine(String machineName, String machineAddress) {
        this.machineName = machineName;
        this.machineAddress = machineAddress;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineAddress() {
        return machineAddress;
    }

    public void setMachineAddress(String machineAddress) {
        this.machineAddress = machineAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        machine saad = (machine) o;
        return machineName.equals(saad.getMachineName()) &&
                machineAddress.equals(saad.getMachineAddress());
    }

}
