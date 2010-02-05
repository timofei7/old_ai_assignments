'''
Created on Feb 4, 2010

@author: tim
for cs44 w10
'''

from State import State
from Computer import Computer
import sys

if __name__ == '__main__':
    if len(sys.argv) > 1:
        file = sys.argv[1]
    else:
        print "usage: eval-c4 filenamewithstate"
        sys.exit(0)
    
    lines = open(file).readlines()
    
    s = State()
    c = Computer(s)
    
    s.decode("".join(lines))
    
    segs = s.build_segments()
    print c.evaluate(segs)