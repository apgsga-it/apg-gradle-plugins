import os
import subprocess
from collections import OrderedDict

root = os.getcwd()


def log(out, adir):
    if "[INFO] BUILD SUCCESS\n[INFO]" in str(out, "UTF-8"):
        print("SUCCESS for %s " % adir)
    else:
        with open("log.log", "wb") as f:
            f.write(out)
        print("FAILED for %s see %s" % (adir, adir + "/log.log"))


def run(aDir):
    os.chdir(aDir)
    process = subprocess.Popen(
        "mvn clean install",
        shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
    )
    out, err = process.communicate()
    log(out, aDir)
    errcode = process.returncode
    return out, err, errcode


def get_items(aDir):
    sorted_list = sorted(
        filter(
            lambda x: not os.path.isfile(os.path.join(aDir, x)) and not x.startswith(".")
            , [i for i in os.listdir(aDir)]
        )
    )
    sorted_list = [os.path.join(root, i) for i in sorted_list]
    return OrderedDict(zip(range(0, len(sorted_list)), sorted_list))


if __name__ == "__main__":
    display_items = get_items(root)
    # print options
    list(map(print, ["%s   %s" % (k, v) for k, v in display_items.items()]))
    # ask for projects to compile
    input_ = eval(input("choose projects coma separated:\n "))
    # build projects!
    list(map(run, [display_items[int(i)] for i in input_]))
