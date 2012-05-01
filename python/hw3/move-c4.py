'''
Created on Feb 4, 2010

@author: tim
for cs44 w10

this takes two arguments who is going next and a file with state info
'''

from State import State
from Computer import Computer
import sys

if __name__ == '__main__':
    if len(sys.argv) > 1:
        who = sys.argv[1]
        file = sys.argv[2]
    else:
        print "usage: move-c4 whosmove filenamewithstate"
        sys.exit(0)
    
    lines = open(file).readlines()
    
    s = State()
    c = Computer(s)
    
    s.decode("".join(lines))
    
    print "utility: " + str(c.utility(s))
    print "move chosen " + str(c.minimaxi(s, who))