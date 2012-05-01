'''
Created on Feb 2, 2010

@author: tim
for cs44 w10
'''

from State import State
import sys

if __name__ == '__main__':
    if len(sys.argv) > 1:
        file = sys.argv[1]
    else:
        print "usage: legal-c4 filenamewithstate"
        sys.exit(0)
    
    lines = open(file).readlines()
    
    s = State()
    s.decode("".join(lines))
    print s.legal_moves()
        
