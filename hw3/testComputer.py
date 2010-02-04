'''
Created on Feb 3, 2010

@author: tim
'''
from Computer import Computer
from State import State

if __name__ == '__main__':
    
    t = """
-1234567-
|.......|
|.......|
|.......|
|.......|
|..O....|
|.OXXX..|
-1234567-"""

    
    s = State()
    s.decode(t)
    
    c = Computer(s)
    print s.allsegs
    s.eval("X")
    #print s.allsegs
    #print len(s.allsegs)
    #print c.minimaxi(s)
    