package com.apgsga.ssh.common

class Cmd {

    boolean isSudo = false

    String sshCmd

    boolean pty = true

    boolean printResult = false

    private Cmd(){}

    static Cmd create() {
        new Cmd()
    }

    def isSudo(boolean isSudo) {
        this.isSudo = isSudo
        this
    }

    def sshCmd(String sshCmd) {
        this.sshCmd = sshCmd
        this
    }

    def pty(boolean pty) {
        this.pty = pty
        this
    }

    def printResult(boolean printResult) {
        this.printResult = printResult
        this
    }
}
