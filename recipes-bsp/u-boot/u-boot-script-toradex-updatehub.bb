LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "u-boot-mkimage-native"

SRC_URI = "file://bootscript.txt"

inherit deploy

do_compile[noexec] = "1"

do_mkimage () {
    uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
                  -n "boot script" -d ${WORKDIR}/bootscript.txt \
                  ${B}/boot.scr
}

addtask mkimage after do_compile before do_install

do_install () {
    install -Dm 0644 ${B}/boot.scr ${D}/boot.scr
}

do_deploy () {
    install -Dm 0644 ${D}/boot.scr ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}
    cd ${DEPLOYDIR}
    rm -f boot.scr-${MACHINE}
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} boot.scr-${MACHINE}
}

addtask deploy after do_install before do_build

FILES_${PN} += "/"

PROVIDES += "u-boot-default-script"
RPROVIDES_${PN} += "u-boot-default-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(apalis-imx6|colibri-imx6|colibri-imx7-emmc)"