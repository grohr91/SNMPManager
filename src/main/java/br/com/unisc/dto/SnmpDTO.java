/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.unisc.dto;

import java.util.List;

/**
 * @author Douglas Reinicke
 * @author Guilherme Rohr
 */
public class SnmpDTO {

    private String hostIp, sysDescr, sysUpTime;
    private Double memTotal, memFree, diskTotal, diskUsed;
    private List<String> softwares;

    public SnmpDTO(String ip) {
        hostIp = ip;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getSysDescr() {
        return sysDescr;
    }

    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    public String getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(String sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public Double getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Double memTotal) {
        if (memTotal != null) {
            this.memTotal = memTotal;
        }
        this.memTotal = memTotal;
    }

    public Double getMemFree() {
        return (memFree / 1024) / 1024;
    }

    public void setMemFree(Double memFree) {
        this.memFree = memFree;
    }

    public List<String> getSoftwares() {
        return softwares;
    }

    public void setSoftwares(List<String> softwares) {
        this.softwares = softwares;
    }

    public Double getDiskTotal() {
        return diskTotal;
    }

    public Double getMemUsed() {
        return ((this.memTotal - this.memFree) / 1024) / 1024;
    }

    public void setDiskTotal(Double diskTotal) {
        this.diskTotal = diskTotal;
    }

    public Double getDiskUsed() {
        return ((diskUsed) / 1024) / 1024;
    }

    public void setDiskUsed(Double diskUsed) {
        this.diskUsed = diskUsed;
    }

    public Double getDiskFree() {
        if (diskTotal == null || diskUsed == null) {
            return 0.0;
        }
        return ((diskTotal - diskUsed) / 1024) / 1024;
    }

}
