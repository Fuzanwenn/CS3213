def echoechoechoecho():
    return 3 == 2

def echoechoecho():
    return echoechoechoecho()

def echo():
    return echoechoecho()

def echoecho():
    return echo()

def main():
    print(echoecho())
    return 0
